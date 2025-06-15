package core.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import core.util.RawImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Bitmap
import kotlin.time.Duration.Companion.milliseconds

private val DefaultAnimationDuration = 100.milliseconds
private val ImageBlank = ImageBitmap(1, 1)

@Composable
fun RawImage.Animated.animatedImageBitmap(): ImageBitmap {

    val animationFlow = remember(data) { animateImageBitmap() }

    return animationFlow.collectAsState(
        initial = ImageBlank
    ).value
}

fun RawImage.Animated.animateImageBitmap() = callbackFlow {

    val bitmap = Bitmap().apply {
        allocPixels(codec.imageInfo)
    }

    while (true) {
        repeat(times = codec.frameCount - 1) { index ->

            codec.readPixels(bitmap, index)

            withContext(Dispatchers.Main) {
                send(bitmap.asComposeImageBitmap())
            }

            val frameDuration = codec.framesInfo[index].duration.milliseconds

            delay(frameDuration.takeOrElse { DefaultAnimationDuration })
        }
    }
}.flowOn(Dispatchers.Default)


