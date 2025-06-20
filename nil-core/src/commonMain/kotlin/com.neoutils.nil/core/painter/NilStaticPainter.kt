package com.neoutils.nil.core.painter

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

class NilStaticPainter(
    private val painter: Painter,
    private var alpha: Float = DefaultAlpha,
    private var colorFilter: ColorFilter? = null
) : NilPainter() {

    override val intrinsicSize = painter.intrinsicSize

    override fun DrawScope.onDraw() {
        painter.run {
            draw(
                size = this@onDraw.size,
                alpha = alpha,
                colorFilter = colorFilter
            )
        }
    }

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }
}