package com.neoutils.nil.decoder.svg.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import com.caverock.androidsvg.SVG
import com.neoutils.nil.core.painter.NilComposeDelegatePainter
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.painter.NilStaticPainter

class NilSvgComposePainter(
    private val svg: SVG
) : NilComposeDelegatePainter() {
    @Composable
    override fun delegate(): NilPainter {

        val density = LocalDensity.current

        return remember(density) { NilStaticPainter(SvgPainter(svg, density)) }
    }
}