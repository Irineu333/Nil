package com.neoutils.nil.decoder.lottie.painter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.painter.DelegatePainter
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottiePainter

internal class LottieComposePainter(
    private val composition: LottieComposition,
    private val iterations: Int? = null,
    private val speed: Float? = null,
) : DelegatePainter() {

    @Composable
    override fun delegate(): Painter {

        val progress by animateLottieCompositionAsState(
            composition = composition,
            speed = speed ?: composition.speed,
            iterations = iterations ?: composition.iterations,
        )

        return rememberLottiePainter(
            composition = composition,
            progress = { progress },
        )
    }
}