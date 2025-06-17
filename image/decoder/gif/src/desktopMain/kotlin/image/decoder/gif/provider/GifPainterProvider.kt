package image.decoder.gif.provider

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import image.core.provider.PainterProvider
import image.core.extension.takeOrElse
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import kotlin.time.Duration.Companion.milliseconds

private val DefaultAnimationDuration = 100.milliseconds
private val ImageBlank = ImageBitmap(1, 1)
private val PainterBlank = BitmapPainter(ImageBlank)

internal class GifPainterProvider(
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