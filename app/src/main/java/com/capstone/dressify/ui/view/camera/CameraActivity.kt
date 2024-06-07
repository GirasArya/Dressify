package com.capstone.dressify.ui.view.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.capstone.dressify.databinding.ActivityCameraBinding
import com.capstone.dressify.helpers.createCustomTempFile
import com.capstone.dressify.helpers.getImageUri
import com.capstone.dressify.ui.view.imagedetail.ImageDetailActivity
import com.capstone.dressify.ui.view.main.MainActivity

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var currentImageUri: Uri? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
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

        changeStatusBarColor("#007BFF")

        val productImage = intent.getStringExtra("PRODUCT_IMAGE")
        val productTitle = intent.getStringExtra("PRODUCT_TITLE")

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        } else {
            startCamera()
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

        binding.ivSwitchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

        binding.ivCapture.setOnClickListener {
            takePhoto()
        }
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

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
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