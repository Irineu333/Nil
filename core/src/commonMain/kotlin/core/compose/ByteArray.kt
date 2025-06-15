package core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import core.extension.animatedImageBitmap
import core.util.RawImage
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.decodeToImageVector
import org.jetbrains.compose.resources.decodeToSvgPainter

@Composable
fun RawImage.resolveAsPainter(): Painter {

    val density = LocalDensity.current

    return when (this) {
        is RawImage.Animated -> {
            val imageBitmap = animatedImageBitmap()
            BitmapPainter(imageBitmap)
        }
        is RawImage.Static -> {
            val imageBitmap = remember(data) { data.decodeToImageBitmap() }
            BitmapPainter(imageBitmap)
        }
        is RawImage.Svg -> {
            remember(data) { data.decodeToSvgPainter(density) }
        }
        is RawImage.Vector -> {
            val imageVector = remember(data) { data.decodeToImageVector(density) }
            rememberVectorPainter(imageVector)
        }
    }
}