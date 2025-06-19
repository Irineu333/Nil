package com.neoutils.nil.decoder.gif.provider

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Movie
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.provider.PainterProvider
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

private val DefaultAnimationDuration = 60.milliseconds
private val ImageBlank = ImageBitmap(1, 1)
private val PainterBlank = BitmapPainter(ImageBlank)

@Suppress("DEPRECATION")
internal class GifPainterLegacyProvider(
    private val movie: Movie
) : PainterProvider {

    constructor(bytes: ByteArray) : this(
        movie = Movie.decodeStream(bytes.inputStream())
    )

    private val bitmap = createBitmap(
        movie.width(),
        movie.height(),
        Bitmap.Config.ARGB_8888
    )

    @Composable
    override fun provide(): Painter {

        var painter by remember { mutableStateOf(PainterBlank) }

        LaunchedEffect(Unit) {
            while (true) {
                val times = 0.until(movie.duration()).step(
                    DefaultAnimationDuration.toInt(
                        DurationUnit.MILLISECONDS
                    )
                )

                for (time in times) {

                    movie.setTime(time)

                    movie.draw(Canvas(bitmap), 0f, 0f)

                    painter = BitmapPainter(bitmap.asImageBitmap())

                    delay(DefaultAnimationDuration.toLong(DurationUnit.MILLISECONDS))
                }
            }
        }

        return painter
    }
}