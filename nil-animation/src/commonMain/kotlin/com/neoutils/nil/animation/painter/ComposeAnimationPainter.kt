package com.neoutils.nil.animation.painter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

abstract class DelegateAnimationPainter : Painter() {

    override fun DrawScope.onDraw() {
        error("Do not run directly")
    }

    override val intrinsicSize by lazy {
        error("Do not run directly")
    }

    @Composable
    abstract fun animate(): Painter
}