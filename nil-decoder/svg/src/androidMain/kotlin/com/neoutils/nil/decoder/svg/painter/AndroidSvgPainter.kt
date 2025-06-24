package com.neoutils.nil.decoder.svg.painter

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import com.caverock.androidsvg.PreserveAspectRatio
import com.caverock.androidsvg.SVG

class AndroidSvgPainter(
    private val svg: SVG,
    private val density: Density
) : Painter() {

    private val defaultSize: Size? = Size(
        svg.documentWidth,
        svg.documentHeight
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
        drawIntoCanvas { canvas ->
            if (svg.documentViewBox == null) {
                svg.setDocumentViewBox(0f, 0f, svg.documentWidth, svg.documentHeight)
            }
            svg.documentWidth = size.width
            svg.documentHeight = size.height
            svg.documentPreserveAspectRatio = PreserveAspectRatio.STRETCH
            svg.renderToCanvas(canvas.nativeCanvas)
        }
    }
}