package com.neoutils.nil.decoder.svg.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import com.caverock.androidsvg.SVG
import com.neoutils.nil.core.painter.DelegatePainter

class SvgDelegatePainter(
    private val svg: SVG
) : DelegatePainter() {
    @Composable
    override fun delegate(): Painter {

        val density = LocalDensity.current

        return remember(density) { SvgPainter(svg, density) }
    }
}