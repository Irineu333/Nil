package com.neoutils.nil.core.painter

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

object EmptyPainter : Painter() {
    override val intrinsicSize get() = Size.Companion.Unspecified
    override fun DrawScope.onDraw() = Unit
}
