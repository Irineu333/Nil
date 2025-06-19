package com.neoutils.nil.decoder.svg.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.provider.PainterProvider
import org.jetbrains.compose.resources.decodeToSvgPainter

internal class SvgPainterProvider(
    private val bytes: ByteArray
) : PainterProvider {
    @Composable
    override fun provide(): Painter {

        val density = LocalDensity.current

        return remember(bytes, density) { bytes.decodeToSvgPainter(density) }
    }
}