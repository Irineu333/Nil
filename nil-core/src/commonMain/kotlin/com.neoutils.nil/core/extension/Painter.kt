package com.neoutils.nil.core.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.painter.DelegatePainter
import com.neoutils.nil.core.util.PainterResource

fun Result<Painter>.toPainter(): PainterResource.Result {
    return if (isSuccess) {
        PainterResource.Result.Success(getOrThrow())
    } else {
        PainterResource.Result.Failure(checkNotNull(exceptionOrNull()))
    }
}

@Composable
internal fun Painter.delegate(): Painter {
    return when (this) {

        is DelegatePainter -> delegate()

        else -> this
    }
}

@Composable
internal fun PainterResource.delegate() = when (this) {
    is PainterResource.Loading -> copy(painter = painter.delegate())
    is PainterResource.Result.Failure -> copy(painter = painter.delegate())
    is PainterResource.Result.Success -> copy(painter = painter.delegate())
}

fun PainterResource.copy(
    painter: Painter = this.painter
) = when (this) {
    is PainterResource.Loading -> copy(painter = painter)
    is PainterResource.Result.Failure -> copy(painter = painter)
    is PainterResource.Result.Success -> copy(painter = painter)
}
