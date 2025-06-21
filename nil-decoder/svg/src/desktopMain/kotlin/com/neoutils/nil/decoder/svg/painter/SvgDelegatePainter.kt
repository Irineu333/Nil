package com.neoutils.nil.decoder.svg.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.painter.DelegatePainter
import org.jetbrains.compose.resources.decodeToSvgPainter

class SvgDelegatePainter(
    private val bytes: ByteArray
) : DelegatePainter() {
    @Composable
    override fun delegate(): Painter {

        val density = LocalDensity.current

        return remember(bytes, density) { bytes.decodeToSvgPainter(density) }
    }
}