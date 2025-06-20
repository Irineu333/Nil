package com.neoutils.nil.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.neoutils.nil.core.compose.asyncAnimatedPainterResource
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Resource

@Composable
fun AsyncImage(
    input: Input,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    loading: Painter? = null,
    error: Painter? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    settings: SettingsScope.() -> Unit = {}
) {
    val resource = asyncAnimatedPainterResource(
        input = input,
        settings = settings
    )

    when(resource) {
        is Resource.Loading -> loading
        is Resource.Result.Failure -> error
        is Resource.Result.Success<Painter> -> resource.data
    }?.let {
        Image(
            painter = it,
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter
        )
    }
}