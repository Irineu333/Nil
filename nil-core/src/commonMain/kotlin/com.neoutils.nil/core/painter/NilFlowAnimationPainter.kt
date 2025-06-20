package com.neoutils.nil.core.painter

import androidx.compose.ui.graphics.painter.Painter
import kotlinx.coroutines.flow.Flow

abstract class NilFlowAnimationPainter : NilPainter() {
    abstract val flow: Flow<Painter>
}