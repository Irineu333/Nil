package com.neoutils.nil.decoder.gif.impl

import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.graphics.drawable.toBitmap
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.extension.startsWith
import com.neoutils.nil.decoder.gif.util.signature
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private val DefaultAnimationDuration = 10.milliseconds
private val ImageBlank = ImageBitmap(1, 1)
private val PainterBlank = BitmapPainter(ImageBlank)

// https://en.wikipedia.org/wiki/List_of_file_signatures
private val GIF87A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x37, 0x61)
private val GIF89A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x39, 0x61)

@RequiresApi(Build.VERSION_CODES.P)
actual class GifDecoder : Decoder {
    actual override fun decode(input: ByteArray): PainterProvider {

        val drawable = AnimatedImageDrawable.createFromStream(input.inputStream(), null) as AnimatedImageDrawable

        return object : PainterProvider {

            @Composable
            override fun provide(): Painter {

                var painter by remember { mutableStateOf(PainterBlank) }

                LaunchedEffect(Unit) {

                    drawable.start()

                    while (drawable.isRunning) {
                        painter = BitmapPainter(drawable.toBitmap().asImageBitmap())
                        delay(DefaultAnimationDuration)
                    }
                }

                return painter
            }
        }
    }

    actual override fun support(input: ByteArray): Support {
        if (input.isEmpty()) return Support.NONE

        return when {
            input.startsWith(GIF87A_SPEC) -> Support.TOTAL
            input.startsWith(GIF89A_SPEC) -> Support.TOTAL
            else -> Support.NONE
        }
    }
}