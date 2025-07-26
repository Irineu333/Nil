package com.neoutils.nil.decoder.gif.painter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.unit.toSize
import com.neoutils.nil.core.extension.takeOrElse
import com.neoutils.nil.core.painter.Animatable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import kotlin.time.Duration.Companion.milliseconds

// https://github.com/JetBrains/compose-multiplatform/tree/76681da4912de84bc2f94050d2ba970604f54d3e/components/AnimatedImage/library/src/desktopMain/kotlin/org/jetbrains/compose/animatedimage

private val DefaultAnimationDuration = 100.milliseconds

internal class SkiaGifPainter(
    private val codec: Codec,
    private val repeatCount: Int = Int.MAX_VALUE
) : Painter(), Animatable {

    override val intrinsicSize = IntSize(
        width = codec.width,
        height = codec.height
    ).toSize()

    private val bitmap = Bitmap().also {
        it.allocPixels(codec.imageInfo)
        codec.readPixels(it, 0)
    }

    private var imageBitmap by mutableStateOf(bitmap.asComposeImageBitmap())
    private var alpha: Float by mutableFloatStateOf(DefaultAlpha)
    private var colorFilter: ColorFilter? by mutableStateOf(null)

    private var interactions = 0

    override suspend fun animate() = coroutineScope {
        while (isActive && interactions++ <= repeatCount) {
            for ((index, frame) in codec.framesInfo.withIndex()) {

                codec.readPixels(bitmap, index)
                imageBitmap = bitmap.asComposeImageBitmap()

                val frameDuration = frame.duration.milliseconds

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
