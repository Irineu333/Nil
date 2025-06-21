package com.neoutils.nil.animation.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.animation.painter.DelegateAnimationPainter
import com.neoutils.nil.animation.painter.FlowAnimationPainter
import com.neoutils.nil.core.util.PainterResource

@Composable
fun Painter.animateAsPainter(): Painter {
    return when (this) {
        is FlowAnimationPainter -> {
            remember(this) { flow }.collectAsState(this).value
        }

        is DelegateAnimationPainter -> animate()

        else -> this
    }
}

@Composable
fun PainterResource.animateAsPainter() = when (this) {
    is PainterResource.Loading -> copy(painter = painter.animateAsPainter())
    is PainterResource.Result.Failure -> copy(painter = painter.animateAsPainter())
    is PainterResource.Result.Success -> copy(painter = painter.animateAsPainter())
}
