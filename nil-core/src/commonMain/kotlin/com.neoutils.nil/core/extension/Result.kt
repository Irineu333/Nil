package com.neoutils.nil.core.extension

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.util.PainterResource

fun Result<Painter>.toPainterResource(): PainterResource.Result {
    return if (isSuccess) {
        PainterResource.Result.Success(getOrThrow())
    } else {
        PainterResource.Result.Failure(checkNotNull(exceptionOrNull()))
    }
}