package core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import core.codec.AnimationDecoder
import core.codec.ImageBitmapDecoder
import core.codec.SvgPainterDecoder
import core.codec.XmlVectorDecoder
import core.util.RawImage
import kotlinx.coroutines.flow.map

private val ImageBlank = ImageBitmap(1, 1)
private val PainterBlank = BitmapPainter(ImageBlank)

@Composable
fun RawImage.resolveAsPainter(): Painter {

    return when (this) {
        is RawImage.Animated -> {
            val decoder = remember { AnimationDecoder() }
            val bitmapFlow = remember(data) { decoder.decode(codec).map { BitmapPainter(it) } }
            bitmapFlow.collectAsState(initial = PainterBlank).value
        }

        is RawImage.Static -> {
            val decoder = remember { ImageBitmapDecoder() }
            remember(data) { BitmapPainter(decoder.decode(data)) }
        }

        is RawImage.Svg -> {
            val density = LocalDensity.current
            val decoder = remember(density) { SvgPainterDecoder(density) }
            remember(data) { decoder.decode(data) }
        }

        is RawImage.Vector -> {
            val density = LocalDensity.current
            val decoder = remember(density) { XmlVectorDecoder(density) }
            val imageVector = remember(data) { decoder.decode(data) }
            rememberVectorPainter(imageVector)
        }
    }
}
