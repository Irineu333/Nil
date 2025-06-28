package com.neoutils.nil.decoder.gif.painter

import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.drawable.toBitmap

class DrawablePainter(
    private val drawable: Drawable,
) : Painter() {

    override val intrinsicSize = IntSize(
        width = drawable.intrinsicWidth,
        height = drawable.intrinsicHeight
    ).toSize()

    private val imageBitmap by lazy { drawable.toBitmap().asImageBitmap() }

    private var alpha: Float by mutableFloatStateOf(DefaultAlpha)
    private var colorFilter: ColorFilter? by mutableStateOf(null)

    override fun DrawScope.onDraw() {
        drawImage(
            image = imageBitmap,
            dstSize = size.toIntSize(),
            colorFilter = colorFilter,
            alpha = alpha
        )
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