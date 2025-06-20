package com.neoutils.nil.decoder.xml.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.painter.NilComposeDelegatePainter
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.painter.NilStaticPainter
import org.jetbrains.compose.resources.decodeToImageVector

class NilXmlPainter(
    private val input: ByteArray
) : NilComposeDelegatePainter() {
    @Composable
    override fun delegate(): NilPainter {

        val density = LocalDensity.current

        val imageVector = remember(input, density) { input.decodeToImageVector(density) }

        val painter = rememberVectorPainter(imageVector)

        return remember(painter) { NilStaticPainter(painter) }
    }
}