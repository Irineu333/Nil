package com.neoutils.nil.core.painter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.DrawScope

abstract class NilComposeDelegatePainter : NilPainter() {

    override fun DrawScope.onDraw() {
        error("Use get composable")
    }

    override val intrinsicSize by lazy {
        error("Use get composable")
    }

    @Composable
    abstract fun delegate(): NilPainter
}