package com.neoutils.nil.decoder.xml.painter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.toPath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize

class VectorPainter(
    private val imageVector: ImageVector,
    private val density: Density
) : Painter() {

    private var alpha by mutableStateOf(1f)
    private var colorFilter by mutableStateOf<ColorFilter?>(null)

    override val intrinsicSize: Size
        get() = density.run {
            DpSize(
                width = imageVector.defaultWidth,
                height = imageVector.defaultHeight
            ).toSize()
        }

    override fun DrawScope.onDraw() {
        val scaleX = size.width / imageVector.viewportWidth
        val scaleY = size.height / imageVector.viewportHeight

        scale(scaleX, scaleY, pivot = Offset.Zero) {
            drawVectorGroup(imageVector.root)
        }
    }

    private fun DrawScope.drawVectorGroup(group: VectorGroup) {
        group.forEach { node ->
            when (node) {
                is VectorPath -> drawVectorPath(node)
                is VectorGroup -> drawVectorGroup(node)
            }
        }
    }

    private fun DrawScope.drawVectorPath(path: VectorPath) {

        path.fill?.let { brush ->
            drawPath(
                path = path.pathData.toPath(),
                brush = brush,
                alpha = path.fillAlpha * alpha,
                colorFilter = colorFilter
            )
        }

        path.stroke?.let { brush ->
            drawPath(
                path = path.pathData.toPath(),
                brush = brush,
                alpha = path.strokeAlpha * alpha,
                style = Stroke(
                    width = path.strokeLineWidth,
                    miter = path.strokeLineMiter,
                    cap = path.strokeLineCap,
                    join = path.strokeLineJoin
                ),
                colorFilter = colorFilter
            )
        }
    }

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }
}
