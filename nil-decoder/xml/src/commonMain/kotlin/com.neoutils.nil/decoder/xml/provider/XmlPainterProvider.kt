package com.neoutils.nil.decoder.xml.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.neoutils.nil.core.provider.PainterProvider

internal class XmlPainterProvider(
    private val imageVector: ImageVector
) : PainterProvider {
    @Composable
    override fun provide(): Painter {
        return rememberVectorPainter(imageVector)
    }
}