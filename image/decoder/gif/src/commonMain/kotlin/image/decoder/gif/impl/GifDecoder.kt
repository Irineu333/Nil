package image.decoder.gif.impl

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import image.core.decoder.Decoder
import image.core.decoder.PainterProvider
import image.core.decoder.Support
import image.core.extension.takeOrElse
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import kotlin.time.Duration.Companion.milliseconds

private val ImageBlank = ImageBitmap(1, 1)
private val PainterBlank = BitmapPainter(ImageBlank)

private val GIF87A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x37, 0x61)
private val GIF89A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x39, 0x61)

private val DefaultAnimationDuration = 100.milliseconds

private fun signature(vararg bytes: Int) = bytes.map { it.toByte() }.toByteArray()

private fun ByteArray.startsWith(bytes: ByteArray): Boolean {
    if (this.size < bytes.size) return false
    return bytes.indices.all { i -> this[i] == bytes[i] }
}

class GifDecoder : Decoder {
    override fun decode(input: ByteArray): PainterProvider {

        check(support(input) != Support.NONE) { "Doesn't support" }

        val data = Data.makeFromBytes(input)

        val codec = Codec.makeFromData(data)

        return GifPainterProvider(codec)
    }

    override fun support(input: ByteArray): Support {
        if (input.isEmpty()) return Support.NONE

        return when {
            input.startsWith(GIF87A_SPEC) -> Support.TOTAL
            input.startsWith(GIF89A_SPEC) -> Support.TOTAL
            else -> Support.NONE
        }
    }
}

class GifPainterProvider(
    private val codec: Codec,
) : PainterProvider {

    private val bitmap = Bitmap().apply { allocPixels(codec.imageInfo) }

    @Composable
    override fun provide(): Painter {

        var painter by remember { mutableStateOf(PainterBlank) }

        LaunchedEffect(Unit) {


            while (isActive) {
                repeat(times = codec.frameCount - 1) { index ->

                    codec.readPixels(bitmap, index)

                    painter = BitmapPainter(bitmap.asComposeImageBitmap())

                    val frameDuration = codec.framesInfo[index].duration.milliseconds

                    delay(frameDuration.takeOrElse { DefaultAnimationDuration })
                }
            }
        }

        return painter
    }
}
