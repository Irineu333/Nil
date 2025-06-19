package com.neoutils.nil.decoder.lottie.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.provider.PainterProvider
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

internal class LottiePainterProvider(
    private val spec: LottieCompositionSpec
) : PainterProvider {

    @Composable
    override fun provide(): Painter {

        val composition by rememberLottieComposition { spec }

        val progress by animateLottieCompositionAsState(composition)

        return rememberLottiePainter(
            composition = composition,
            progress = { progress },
        )
    }
}