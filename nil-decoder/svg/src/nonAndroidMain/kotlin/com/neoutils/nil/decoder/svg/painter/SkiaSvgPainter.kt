package com.neoutils.nil.decoder.svg.painter

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import org.jetbrains.skia.Rect
import org.jetbrains.skia.svg.SVGDOM
import org.jetbrains.skia.svg.SVGLength
import org.jetbrains.skia.svg.SVGLengthUnit
import org.jetbrains.skia.svg.SVGPreserveAspectRatio
import org.jetbrains.skia.svg.SVGPreserveAspectRatioAlign

class SkiaSvgPainter(
    private val dom: SVGDOM,
    private val density: Density
) : Painter() {

    private val root = dom.root

    private val defaultSize: Size? = root?.let {
        Size(
            it.width.withUnit(SVGLengthUnit.PX).value,
            it.height.withUnit(SVGLengthUnit.PX).value
        )
    }?.takeUnless {
        it.height == 0f && it.width == 0f
    }

    init {
        if (root?.viewBox == null && defaultSize != null) {
            root?.viewBox = Rect.makeXYWH(0f, 0f, defaultSize.width, defaultSize.height)
        }
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
            root?.width = SVGLength(size.width, SVGLengthUnit.PX)
            root?.height = SVGLength(size.height, SVGLengthUnit.PX)
            root?.preserveAspectRatio = SVGPreserveAspectRatio(SVGPreserveAspectRatioAlign.NONE)
            dom.render(canvas.nativeCanvas)
        }
    }
}