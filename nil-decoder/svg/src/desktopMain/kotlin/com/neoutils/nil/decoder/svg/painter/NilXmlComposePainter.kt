package com.neoutils.nil.decoder.svg.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.nil.core.painter.NilComposeDelegatePainter
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.painter.NilStaticPainter
import org.jetbrains.compose.resources.decodeToSvgPainter

class NilXmlComposePainter(
    private val bytes: ByteArray
) : NilComposeDelegatePainter() {
    @Composable
    override fun delegate(): NilPainter {

        val density = LocalDensity.current

        return remember(bytes, density) { NilStaticPainter(bytes.decodeToSvgPainter(density)) }
    }
}