@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.extension.copy
import com.neoutils.nil.core.extension.delegate
import com.neoutils.nil.core.model.Nil
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.scope.rememberSettings
import com.neoutils.nil.core.util.EmptyPainter
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.PainterResource
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun asyncPainterResource(
    input: Input,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter,
    settings: SettingsScope.() -> Unit = {}
): PainterResource {

    val settings = rememberSettings(settings)

    val flow = remember(settings, input) { Nil(settings).execute(input) }

    val painter by flow.collectAsState(PainterResource.Loading())

    return when (painter) {
        is PainterResource.Result.Success -> painter.delegate()

        is PainterResource.Loading -> {
            painter.copy(painter = placeholder)
        }

        is PainterResource.Result.Failure -> {
            painter.copy(painter = fallback)
        }
    }
}
