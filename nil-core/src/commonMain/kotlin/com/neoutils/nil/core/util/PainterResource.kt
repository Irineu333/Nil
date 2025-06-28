package com.neoutils.nil.core.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

sealed class PainterResource : Painter(), State {

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
    ) : PainterResource() {
        override val isLoading = true
        override val isSuccess = false
        override val isFailure = false
    }

    sealed class Result : PainterResource() {

        data class Success(
            override val painter: Painter
        ) : Result() {
            override val isLoading = false
            override val isSuccess = true
            override val isFailure = false
        }

        data class Failure(
            val throwable: Throwable,
            override val painter: Painter = EmptyPainter
        ) : Result() {
            override val isLoading = false
            override val isSuccess = false
            override val isFailure = true
        }
    }
}
