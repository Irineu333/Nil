package com.neoutils.nil.decoder.gif.painter

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.neoutils.nil.core.painter.NilFlowAnimationPainter
import com.neoutils.nil.core.extension.takeOrElse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import kotlin.time.Duration.Companion.milliseconds

private val DefaultAnimationDuration = 100.milliseconds

class NilGifPainterSkia(
    private val codec: Codec
) : NilFlowAnimationPainter() {

    private val static by lazy {
        BitmapPainter(
            codec.createBitmap(index = 0)
        )
    }

    override val intrinsicSize: Size = IntSize(
        width = codec.width,
        height = codec.height
    ).toSize()

    private var alpha: Float = DefaultAlpha
    private var colorFilter: ColorFilter? = null

    private val frameCache = mutableMapOf<Int, ImageBitmap>()

    override val flow: Flow<Painter>
        get() = callbackFlow {
            while (isActive) {
                repeat(times = codec.frameCount - 1) { index ->

                    val bitmap = frameCache.getOrPut(index) {
                        codec.createBitmap(index)
                    }

                    send(BitmapPainter(bitmap))

                    val frameDuration = codec.framesInfo[index].duration.milliseconds

                    delay(frameDuration.takeOrElse { DefaultAnimationDuration })
                }
            }
            awaitCancellation()
        }.flowOn(Dispatchers.Default)

    override fun DrawScope.onDraw() {
        static.run {
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

private fun Codec.createBitmap(index: Int): ImageBitmap {
    return Bitmap().also {
        it.allocPixels(imageInfo)
        readPixels(it, index)
    }.asComposeImageBitmap()
}