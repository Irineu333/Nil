package com.neoutils.nil.core.extension

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.util.EmptyPainter
import com.neoutils.nil.core.util.PainterResource

fun PainterResource.merge(
    failure: Painter = EmptyPainter,
    loading: Painter = EmptyPainter
): PainterResource {
    return when (this) {
        is PainterResource.Loading -> copy(painter = loading)
        is PainterResource.Result.Failure -> copy(painter = failure)
        is PainterResource.Result.Success -> this
    }
}
