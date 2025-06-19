package com.neoutils.nil.decoder.xml.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.provider.PainterProvider
import org.jetbrains.compose.resources.decodeToImageVector

internal class XmlPainterProvider(
    private val input: ByteArray
) : PainterProvider {
    @Composable
    override fun provide(): Painter {

        val density = LocalDensity.current

        val imageVector = remember(input, density) { input.decodeToImageVector(density) }

        return rememberVectorPainter(imageVector)
    }
}