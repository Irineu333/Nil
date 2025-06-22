package com.neoutils.nil.core.painter

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp

abstract class DelegatePainter : Painter() {

    override fun DrawScope.onDraw() {
        error("Do not run directly")
    }

    override val intrinsicSize = Size.Unspecified

    @Composable
    abstract fun delegate(): Painter
}
