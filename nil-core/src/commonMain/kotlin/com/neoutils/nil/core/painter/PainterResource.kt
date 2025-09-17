package com.neoutils.nil.core.painter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.contract.State

sealed class PainterResource : Painter() {

    internal abstract val painter: Painter

    override val intrinsicSize get() = painter.intrinsicSize

    private var alpha: Float by mutableFloatStateOf(DefaultAlpha)
    private var colorFilter: ColorFilter? by mutableStateOf(null)

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    override fun DrawScope.onDraw() = with(painter) {
        draw(
            size = size,
            alpha = alpha,
            colorFilter = colorFilter,
        )
    }

    data class Loading(
        val progress: Float? = null,
        override val painter: Painter = EmptyPainter
    ) : PainterResource()

    sealed class Result : PainterResource() {

        data class Success(
            override val painter: Painter
        ) : Result()

        data class Failure(
            val throwable: Throwable,
            override val painter: Painter = EmptyPainter
        ) : Result()
    }
}
