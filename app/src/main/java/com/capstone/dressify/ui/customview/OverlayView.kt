package com.capstone.dressify.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.capstone.dressify.ui.view.camera.BoundingBox

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results = listOf<BoundingBox>()
    var imageResources: List<Drawable?> = emptyList()

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        for (i in results.indices) {
            val result = results[i] // Get the current bounding box
            val imageResource = imageResources.getOrNull(i) // Get the drawable for this box

            if (imageResource != null) {
                // Calculate the scaled image size
                val shoulderWidth =
                    (result.x2 - result.x1) * width * 1.15f // Adjust this value as needed
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
}
