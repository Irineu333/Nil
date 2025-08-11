package com.neoutils.nil.core.composable

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.painter.EmptyPainter
import com.neoutils.nil.core.scope.SettingsScope

@Composable
fun Image(
    request: Request.Sync,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fallback: Painter = EmptyPainter,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    settings: SettingsScope.() -> Unit = {}
) {
    val painter = painterResource(
        request = request,
        fallback = fallback,
        settings = settings
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}
