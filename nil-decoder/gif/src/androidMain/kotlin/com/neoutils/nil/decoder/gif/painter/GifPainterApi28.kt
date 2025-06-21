package com.neoutils.nil.decoder.gif.painter

import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.drawable.toBitmap
import com.neoutils.nil.core.painter.PainterAnimation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds

private val DefaultAnimationDuration = 10.milliseconds

@RequiresApi(Build.VERSION_CODES.P)
class GifPainterApi28(
    private val drawable: AnimatedImageDrawable,
) : Painter(), PainterAnimation {

    override val intrinsicSize: Size = IntSize(
        width = drawable.intrinsicWidth,
        height = drawable.intrinsicHeight
    ).toSize()

    private var imageBitmap by mutableStateOf(drawable.toBitmap().asImageBitmap())

    private var alpha: Float = DefaultAlpha
    private var colorFilter: ColorFilter? = null

    override suspend fun animate() = coroutineScope {
        drawable.start()

        while (drawable.isRunning && isActive) {
            imageBitmap = drawable.toBitmap().asImageBitmap()
            delay(DefaultAnimationDuration)
        }

        drawable.stop()
    }


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