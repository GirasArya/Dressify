package com.capstone.dressify.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.capstone.dressify.R
import com.capstone.dressify.ui.view.camera.BoundingBox

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results = listOf<BoundingBox>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()
    var imageResources: List<Drawable?> = emptyList()
    private var bounds = Rect()

    init {
        initPaints()
    }

    fun clear() {
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.bounding_box_color)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        for (i in results.indices) {
            val result = results[i] // Get the current bounding box
            val imageResource = imageResources.getOrNull(i) // Get the drawable for this box

            results.forEach {
                val left = it.x1 * width
                val top = it.y1 * height
                val right = it.x2 * width
                val bottom = it.y2 * height

                canvas.drawRect(left, top, right, bottom, boxPaint)
                val drawableText = it.clsName

                textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
                val textWidth = bounds.width()
                val textHeight = bounds.height()
                canvas.drawRect(
                    left,
                    top,
                    left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                    top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                    textBackgroundPaint
                )
                canvas.drawText(drawableText, left, top + bounds.height(), textPaint)
            }

            if (imageResource != null) {
                // Calculate the scaled image size
                val shoulderWidth =
                    (result.x2 - result.x1) * width * 1.15f // 40% of bounding box width per shoulder
                val shoulderHeight =
                    shoulderWidth * imageResource.intrinsicHeight / imageResource.intrinsicWidth


                // Left shoulder coordinates
                val leftShoulderLeft = result.x1 * width
                val leftShoulderTop = result.y1 * height

                // Right shoulder coordinates
                val rightShoulderLeft = (result.x2 - shoulderWidth) * width
                val rightShoulderTop = result.y1 * height

                // Draw left shoulder image
                imageResource.setBounds(
                    leftShoulderLeft.toInt(),
                    leftShoulderTop.toInt(),
                    (leftShoulderLeft + shoulderWidth).toInt(),
                    (leftShoulderTop + shoulderHeight).toInt()
                )
                imageResource.draw(canvas)

                // Draw right shoulder image
                imageResource.setBounds(
                    rightShoulderLeft.toInt(),
                    rightShoulderTop.toInt(),
                    (rightShoulderLeft + shoulderWidth).toInt(),
                    (rightShoulderTop + shoulderHeight).toInt()
                )
                imageResource.draw(canvas)
            }
        }
    }


    fun setResults(boundingBoxes: List<BoundingBox>) {
        results = boundingBoxes
        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}