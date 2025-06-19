package com.neoutils.nil.decoder.svg.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import com.caverock.androidsvg.SVG
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toResource
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.svg.format.SVG_REGEX
import com.neoutils.nil.decoder.svg.painter.SVGPainter

actual class SvgDecoder : Decoder {

    actual override suspend fun decode(input: ByteArray): Resource.Result<PainterProvider> {

        if (support(input) == Support.NONE) {
            return Resource.Result.Failure(NotSupportException())
        }

        return runCatching {
            SvgPainterProvider(SVG.getFromInputStream(input.inputStream()))
        }.toResource()
    }

    actual override fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(SVG_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}

private class SvgPainterProvider(
    private val svg: SVG
) : PainterProvider {
    @Composable
    override fun provide(): Painter {

        val density = LocalDensity.current

        return remember(density) { SVGPainter(svg, density) }
    }
}

