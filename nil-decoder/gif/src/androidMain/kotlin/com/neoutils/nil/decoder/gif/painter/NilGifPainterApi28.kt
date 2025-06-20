package com.neoutils.nil.decoder.gif.painter

import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.drawable.toBitmap
import com.neoutils.nil.core.painter.NilFlowAnimationPainter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlin.time.Duration.Companion.milliseconds

private val DefaultAnimationDuration = 10.milliseconds

@RequiresApi(Build.VERSION_CODES.P)
class NilGifPainterApi28(
    private val drawable: AnimatedImageDrawable,
) : NilFlowAnimationPainter() {

    override val intrinsicSize: Size = IntSize(
        width = drawable.intrinsicWidth,
        height = drawable.intrinsicHeight
    ).toSize()

    private val static by lazy {
        BitmapPainter(drawable.toBitmap().asImageBitmap())
    }

    private var alpha: Float = DefaultAlpha
    private var colorFilter: ColorFilter? = null

    override val flow: Flow<Painter>
        get() = callbackFlow {
            drawable.start()

            while (drawable.isRunning && isActive) {
                withContext(Dispatchers.Main) {
                    send(
                        BitmapPainter(
                            drawable.toBitmap().asImageBitmap(),
                        ).apply {
                            applyAlpha(alpha)
                            applyColorFilter(colorFilter)
                        }
                    )
                }

                delay(DefaultAnimationDuration)
            }

            drawable.stop()
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