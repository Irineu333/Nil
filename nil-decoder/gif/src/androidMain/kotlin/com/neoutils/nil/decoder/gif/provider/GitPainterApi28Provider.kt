package com.neoutils.nil.decoder.gif.provider

import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.graphics.drawable.toBitmap
import com.neoutils.nil.core.provider.PainterProvider
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private val DefaultAnimationDuration = 10.milliseconds
private val ImageBlank = ImageBitmap(1, 1)
private val PainterBlank = BitmapPainter(ImageBlank)

@RequiresApi(Build.VERSION_CODES.P)
internal class GitPainterApi28Provider(
    private val drawable: AnimatedImageDrawable
) : PainterProvider {

    constructor(bytes: ByteArray) : this(
        drawable = AnimatedImageDrawable.createFromStream(bytes.inputStream(), null) as AnimatedImageDrawable
    )

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