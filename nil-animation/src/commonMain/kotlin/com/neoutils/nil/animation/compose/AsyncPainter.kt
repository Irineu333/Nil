package com.neoutils.nil.animation.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.animation.extension.animateAsPainter
import com.neoutils.nil.core.compose.asyncPainterResource
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.util.EmptyPainter
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.PainterResource

@Composable
fun asyncAnimatedPainterResource(
    input: Input,
    placeholder: Painter = EmptyPainter,
    error: Painter = EmptyPainter,
    settings: SettingsScope.() -> Unit = {}
): PainterResource {
    return asyncPainterResource(
        input = input,
        placeholder = placeholder,
        fallback = error,
        settings = settings
    ).animateAsPainter()
}