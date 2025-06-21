package com.neoutils.nil.core.util

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

sealed class PainterResource : Painter() {

    override val intrinsicSize get() = painter.intrinsicSize

    internal abstract val painter: Painter

    private var colorFilter: ColorFilter? = null

    private var alpha: Float = DefaultAlpha

    override fun DrawScope.onDraw() {
        painter.run {
            draw(
                size = size,
                alpha = alpha,
                colorFilter = colorFilter,
            )
        }
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    data class Loading(
        val progress: Float? = null,
        public override val painter: Painter = EmptyPainter
    ) : PainterResource()

    sealed class Result : PainterResource() {

        data class Success(
            public override val painter: Painter
        ) : Result()

        data class Failure(
            val throwable: Throwable,
            public override val painter: Painter = EmptyPainter
        ) : Result()
    }
}
