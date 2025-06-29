package com.neoutils.nil.decoder.gif.painter

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Movie
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.unit.toSize
import com.neoutils.nil.core.painter.Animatable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

private val DefaultAnimationDuration = 60.milliseconds

@Suppress("DEPRECATION")
internal class MovieGifPainter(
    private val movie: Movie,
    private val repeatCount: Int = Int.MAX_VALUE
) : Painter(), Animatable {

    override val intrinsicSize = IntSize(
        width = movie.width(),
        height = movie.height()
    ).toSize()

    private val bitmap = createBitmap(
        movie.width(),
        movie.height(),
        Bitmap.Config.ARGB_8888
    )

    private val frameCache = mutableMapOf<Int, ImageBitmap>()

    private var imageBitmap by mutableStateOf(createFrameBitmap(time = 0))
    private var alpha: Float by mutableFloatStateOf(DefaultAlpha)
    private var colorFilter: ColorFilter? by mutableStateOf(null)

    private var interactions = 0

    override suspend fun animate() = coroutineScope {
        while (isActive && interactions++ <= repeatCount) {

            val times = 0.until(movie.duration()).step(
                DefaultAnimationDuration.toInt(
                    DurationUnit.MILLISECONDS
                )
            )

            for (time in times) {

                imageBitmap = createFrameBitmap(time)

                delay(DefaultAnimationDuration)
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

    private fun createFrameBitmap(time: Int): ImageBitmap {

        movie.setTime(time)

        return frameCache.getOrPut(time) {
            movie.draw(Canvas(bitmap), 0f, 0f)
            bitmap.asImageBitmap()
        }
    }
}