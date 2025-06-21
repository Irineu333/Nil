package com.neoutils.nil.decoder.gif.painter

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Movie
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.neoutils.nil.animation.painter.FlowAnimationPainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

private val DefaultAnimationDuration = 60.milliseconds

@Suppress("DEPRECATION")
class GifPainterLowerApi(
    private val movie: Movie,
    private val static: Painter = BitmapPainter(
        createBitmap(
            movie.width(),
            movie.height(),
            Bitmap.Config.ARGB_8888
        ).also {
            movie.draw(Canvas(it), 0f, 0f)
        }.asImageBitmap()
    )
) : FlowAnimationPainter() {

    override val intrinsicSize: Size = IntSize(
        width = movie.width(),
        height = movie.height()
    ).toSize()

    private var alpha: Float = DefaultAlpha
    private var colorFilter: ColorFilter? = null

    private val frameCache = mutableMapOf<Int, ImageBitmap>()

    override val flow: Flow<Painter>
        get() = callbackFlow {
            while (isActive) {
                val times = 0.until(movie.duration()).step(
                    DefaultAnimationDuration.toInt(
                        DurationUnit.MILLISECONDS
                    )
                )

                for (time in times) {

                    val bitmap = frameCache.getOrPut(time) {

                        movie.setTime(time)

                        createBitmap(
                            movie.width(),
                            movie.height(),
                            Bitmap.Config.ARGB_8888
                        ).also {
                            movie.draw(Canvas(it), 0f, 0f)
                        }.asImageBitmap()
                    }

                    send(BitmapPainter(bitmap))

                    delay(DefaultAnimationDuration)
                }
            }

            awaitCancellation()
        }.flowOn(Dispatchers.Default)

    override fun DrawScope.onDraw() {
        static.run {
            draw(
                size = size,
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