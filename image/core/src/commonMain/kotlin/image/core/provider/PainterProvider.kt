package image.core.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

interface PainterProvider {
    @Composable
    fun provide(): Painter
}