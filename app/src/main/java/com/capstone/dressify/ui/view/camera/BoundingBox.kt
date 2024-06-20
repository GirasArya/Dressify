package com.capstone.dressify.ui.view.camera

data class BoundingBox(
    var x1: Float,
    var y1: Float,
    var x2: Float,
    var y2: Float,
    var cx: Float,
    var cy: Float,
    var w: Float,
    var h: Float,
    var cnf: Float,
    var cls: Int,
    var clsName: String,
    var imageUrl: String
)

