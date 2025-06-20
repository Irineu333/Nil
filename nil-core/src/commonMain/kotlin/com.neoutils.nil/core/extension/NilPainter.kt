package com.neoutils.nil.core.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.painter.NilComposeAnimationPainter
import com.neoutils.nil.core.painter.NilComposeDelegatePainter
import com.neoutils.nil.core.painter.NilFlowAnimationPainter
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.util.Resource

@Composable
internal fun NilPainter.delegate(): NilPainter {
    return when (this) {

        is NilComposeDelegatePainter -> delegate()

        else -> this
    }
}

@Composable
internal fun Resource<NilPainter>.delegate() = mapSuccess { it -> it.delegate() }

@Composable
fun NilPainter.animateAsPainter(): Painter {
    return when (this) {
        is NilFlowAnimationPainter -> {
            remember(this) { flow }.collectAsState(this).value
        }

        is NilComposeAnimationPainter -> animate()

        else -> this
    }
}

@Composable
fun Resource<NilPainter>.animateAsPainter(): Resource<Painter> {
    return mapSuccess { it ->
        it.animateAsPainter()
    }
}
