package com.neoutils.nil.decoder.xml.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.painter.DelegatePainter
import org.jetbrains.compose.resources.decodeToImageVector

internal class XmlDelegatePainter(
    private val input: ByteArray
) : DelegatePainter() {
    @Composable
    override fun delegate(): Painter {

        val density = LocalDensity.current

        val imageVector = remember(input, density) { input.decodeToImageVector(density) }

        return rememberVectorPainter(imageVector)
    }
}