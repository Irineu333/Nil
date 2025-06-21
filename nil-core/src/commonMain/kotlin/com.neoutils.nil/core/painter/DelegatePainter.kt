package com.neoutils.nil.core.painter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

abstract class DelegatePainter : Painter() {

    override fun DrawScope.onDraw() {
        error("Do not run directly")
    }

    override val intrinsicSize by lazy {
        error("Do not run directly")
    }

    @Composable
    abstract fun delegate(): Painter
}
