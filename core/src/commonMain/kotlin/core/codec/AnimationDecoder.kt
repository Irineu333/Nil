package core.codec

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import core.extension.takeOrElse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import kotlin.time.Duration.Companion.milliseconds

private val DefaultAnimationDuration = 100.milliseconds

class AnimationDecoder : Decoder<Codec, Flow<ImageBitmap>> {

    override fun decode(input: Codec) = callbackFlow {

        val bitmap = Bitmap().apply {
            allocPixels(input.imageInfo)
        }

        while (isActive) {
            repeat(times = input.frameCount - 1) { index ->

                input.readPixels(bitmap, index)

                withContext(Dispatchers.Main) {
                    send(bitmap.asComposeImageBitmap())
                }

                val frameDuration = input.framesInfo[index].duration.milliseconds

                delay(frameDuration.takeOrElse { DefaultAnimationDuration })
            }
        }

        awaitClose()
    }.flowOn(Dispatchers.Default)
}