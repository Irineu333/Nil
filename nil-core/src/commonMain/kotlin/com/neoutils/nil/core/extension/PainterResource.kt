package com.neoutils.nil.core.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.painter.DelegatePainter
import com.neoutils.nil.core.util.EmptyPainter
import com.neoutils.nil.core.util.PainterResource

@Composable
internal fun PainterResource.delegate(): PainterResource {
    return when (val painter = painter) {
        is DelegatePainter -> copy(painter = painter.delegate())
        else -> this
    }
}

fun PainterResource.copy(
    painter: Painter = this.painter
) = when (this) {
    is PainterResource.Loading -> copy(painter = painter)
    is PainterResource.Result.Failure -> copy(painter = painter)
    is PainterResource.Result.Success -> copy(painter = painter)
}

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
