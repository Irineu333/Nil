package com.neoutils.nil.decoder.gif.painter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.unit.toSize
import com.neoutils.nil.core.extension.takeOrElse
import com.neoutils.nil.core.painter.PainterAnimation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import kotlin.time.Duration.Companion.milliseconds

private val DefaultAnimationDuration = 100.milliseconds

class GifPainterSkia(
    private val codec: Codec
) : Painter(), PainterAnimation {

    private var imageBitmap by mutableStateOf(codec.createBitmap(index = 0))

    override val intrinsicSize = IntSize(
        width = codec.width,
        height = codec.height
    ).toSize()

    private var alpha: Float by mutableFloatStateOf(DefaultAlpha)
    private var colorFilter: ColorFilter? by mutableStateOf(null)

    private val frameCache = mutableMapOf<Int, ImageBitmap>()

    override suspend fun animate() = coroutineScope {
        while (isActive) {
            repeat(times = codec.frameCount - 1) { index ->

                imageBitmap = frameCache.getOrPut(index) { codec.createBitmap(index) }

                val frameDuration = codec.framesInfo[index].duration.milliseconds

                delay(frameDuration.takeOrElse { DefaultAnimationDuration })
            }
        }
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

private fun Codec.createBitmap(index: Int): ImageBitmap {
    return Bitmap().also {
        it.allocPixels(imageInfo)
        readPixels(it, index)
    }.asComposeImageBitmap()
}