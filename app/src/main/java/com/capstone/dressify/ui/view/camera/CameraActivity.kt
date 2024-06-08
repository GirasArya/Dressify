@file:Suppress("DEPRECATION")

package com.capstone.dressify.ui.view.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.capstone.dressify.R
import com.capstone.dressify.databinding.ActivityCameraBinding
import com.capstone.dressify.helpers.createCustomTempFile
import com.capstone.dressify.helpers.getImageUri
import com.capstone.dressify.ui.view.imagedetail.ImageDetailActivity
import com.capstone.dressify.ui.view.main.MainActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.image.TensorImage
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var currentImageUri: Uri? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var interpreter: Interpreter
    private lateinit var clothesBitmap: Bitmap
    private lateinit var boundingBoxOverlay: BoundingBoxOverlay
    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this, REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted)
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT)
            else
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraExecutor = Executors.newSingleThreadExecutor()

        val model = "final_ar_float32.tflite"
        interpreter = Interpreter(loadModelFile(this, model)); Interpreter.Options()
            .addDelegate(GpuDelegate())
        // Load both models (object detection and clothes overlay)
        interpreter =
            Interpreter(loadModelFile(this, "final_ar_float32.tflite")) // Object detection model
        clothesBitmap = BitmapFactory.decodeResource(resources, R.drawable.test_img)
        boundingBoxOverlay = BoundingBoxOverlay(this)
        val overlayLayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        boundingBoxOverlay.layoutParams = overlayLayoutParams


        binding.pvCameraX.overlay.add(boundingBoxOverlay)
        changeStatusBarColor("#007BFF")


        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        } else {
            startCamera(interpreter)
        }

        binding.flBackArrowCamera.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.ivAccessGallery.setOnClickListener {
            startGallery()
        }

        binding.ivCameraUri.setOnClickListener {
            startCameraUri()
        }


        binding.ivCapture.setOnClickListener {
            takePhoto()
        }

    }

    private fun overlayClothes(frame: Bitmap, detections: List<RectF>): Bitmap {
        val mutableBitmap = frame.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.color = Color.RED

        detections.forEach { box ->
            // Adjust clothing image to match the box dimensions (you'll need to figure out the appropriate scaling logic)
            val resizedClothes = Bitmap.createScaledBitmap(
                clothesBitmap,
                box.width().toInt(),
                box.height().toInt(),
                false
            )
            canvas.drawBitmap(resizedClothes, null, box, paint) // Draw the clothing on the canvas
        }

        return mutableBitmap
    }

    private fun startCamera(interpreter: Interpreter) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
                .also { it.setSurfaceProvider(binding.pvCameraX.surfaceProvider) }

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(640, 640)) // Match model input size (640x640)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()


            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                val image = imageProxy.toBitmap()

                if (image != null) {
                    // Object Detection
                    val resizedBitmap = Bitmap.createScaledBitmap(image, 640, 640, false)
                    val tensorImage = TensorImage(DataType.FLOAT32)
                    tensorImage.load(resizedBitmap)

                    val inputArray = arrayOf(tensorImage.buffer)
                    val outputArray = Array(1) { Array(11) { FloatArray(8400) } }
                    val outputMap = HashMap<Int, Any>()
                    outputMap[0] = outputArray

                    interpreter.runForMultipleInputsOutputs(inputArray, outputMap)

                    // Convert object detection output to bounding boxes (fixed)
                    val boundingBoxes = mutableListOf<RectF>()
                    val results = outputArray[0][0] // Access results directly from the first output set
                    for (i in 0 until results.size step 5) { // Iterate in steps of 5, assuming 5 values per detection
                        val confidence = results[i + 4]
                        if (confidence > 0.5) {
                            val x = results[i] * resizedBitmap.width
                            val y = results[i + 1] * resizedBitmap.height
                            val w = results[i + 2] * resizedBitmap.width
                            val h = results[i + 3] * resizedBitmap.height
                            boundingBoxes.add(RectF(x - w / 2, y - h / 2, x + w / 2, y + h / 2))
                        }
                    }
                    Log.d("Bounding", "Bounding boxes: $boundingBoxes")

                    // Update UI on the main thread
                    runOnUiThread {
//                        binding.pvCameraX.post {
//                            boundingBoxOverlay.updateBoundingBoxes(boundingBoxes)
//                            // Update your ImageView or custom view to display annotatedBitmap
//                        }
                        boundingBoxOverlay.updateBoundingBoxes(boundingBoxes)
                    }
                }
                imageProxy.close()
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (exc: Exception) {
                // Handle exceptions
                Log.d("BINDING", "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(this))

        binding.ivSwitchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera(interpreter)
        }

    }

    class BoundingBoxOverlay(context: Context) : View(context) {
        private val boundingBoxes = mutableListOf<RectF>()
        private val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }

        fun updateBoundingBoxes(boxes: List<RectF>) {
            boundingBoxes.clear()
            boundingBoxes.addAll(boxes)
            Log.d(TAG, "Updating bounding boxes: $boundingBoxes") // Log the updated boxes
            invalidate() // Trigger redraw
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            for (box in boundingBoxes) {
                canvas.drawRect(box, paint)
            }
            Log.d("draw", "render to view")
        }
    }

    private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val channel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return channel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun startCameraUri() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let { uri ->
                val intent = Intent(this@CameraActivity, ImageDetailActivity::class.java).apply {
                    putExtra(IMAGE_URI, uri.toString())
                }
                startActivity(intent)
            }
        }
    }


    //Gallery
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            val intent = Intent(this@CameraActivity, ImageDetailActivity::class.java).apply {
                putExtra(IMAGE_URI, currentImageUri.toString())
            }
            startActivity(intent)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(application)
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        if (photoFile == null) {
            Toast.makeText(
                this,
                "Failed to create temporary file for image capture",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_CAMERAX_IMAGE, output.savedUri.toString())
                    setResult(CAMERAX_RESULT, intent)
                    finish()
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }


    private fun changeStatusBarColor(color: String) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val TAG = "CameraActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
        const val IMAGE_URI = "image_uri"
    }
}