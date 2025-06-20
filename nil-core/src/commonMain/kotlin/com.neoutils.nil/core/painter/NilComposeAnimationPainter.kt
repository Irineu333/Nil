package com.neoutils.nil.core.painter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

abstract class NilComposeAnimationPainter : NilPainter() {

    override fun DrawScope.onDraw() {
        error("Use animate composable")
    }

    override val intrinsicSize by lazy {
        error("Use animate composable")
    }

    @Composable
    abstract fun animate(): Painter
}