package com.neoutils.nil.decoder.svg.painter

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import com.caverock.androidsvg.PreserveAspectRatio
import com.caverock.androidsvg.SVG
import kotlin.math.ceil

internal class SVGPainter(
    private val dom: SVG,
    private val density: Density
) : Painter() {

    private val defaultSize: Size? = Size(
        dom.documentWidth,
        dom.documentHeight
    ).takeUnless {
        it.height == 0f && it.width == 0f
    }

    override val intrinsicSize: Size
        get() = when {
            defaultSize != null -> {
                defaultSize * density.density
            }

            else -> Size.Unspecified
        }

    override fun DrawScope.onDraw() {
        drawSvg(
            size = Size(
                ceil(size.width),
                ceil(size.height)
            )
        )
    }

    private fun DrawScope.drawSvg(size: Size) {
        drawIntoCanvas { canvas ->
            if (dom.documentViewBox == null) {
                dom.setDocumentViewBox(0f, 0f, dom.documentWidth, dom.documentHeight)
            }
            dom.documentWidth = size.width
            dom.documentHeight = size.height
            dom.documentPreserveAspectRatio = PreserveAspectRatio.STRETCH
            dom.renderToCanvas(canvas.nativeCanvas)
        }
    }
}