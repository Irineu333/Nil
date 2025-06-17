package image.decoder.bitmap.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import image.core.provider.PainterProvider

internal class BitmapPainterProvider(
    private val imageBitmap: ImageBitmap
): PainterProvider {
    @Composable
    override fun provide(): Painter {
        return remember(imageBitmap) { BitmapPainter(imageBitmap) }
    }
}