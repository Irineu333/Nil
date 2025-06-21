package com.neoutils.nil.animation.painter

import androidx.compose.ui.graphics.painter.Painter
import kotlinx.coroutines.flow.Flow

abstract class FlowAnimationPainter : Painter() {
    abstract val flow: Flow<Painter>
}