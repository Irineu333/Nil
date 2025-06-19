package com.neoutils.nil.decoder.gif.impl

import android.os.Build
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.extension.startsWith
import com.neoutils.nil.decoder.gif.format.GIF87A_SPEC
import com.neoutils.nil.decoder.gif.format.GIF89A_SPEC
import com.neoutils.nil.decoder.gif.provider.GifPainterLegacyProvider
import com.neoutils.nil.decoder.gif.provider.GitPainterApi28Provider

actual class GifDecoder : Decoder {
    actual override fun decode(input: ByteArray): PainterProvider {

        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                GitPainterApi28Provider(input)
            }

            else -> {
                GifPainterLegacyProvider(input)
            }
        }
    }

    actual override fun support(input: ByteArray): Support {
        if (input.isEmpty()) return Support.NONE
        return when {
            input.startsWith(GIF87A_SPEC) -> Support.TOTAL
            input.startsWith(GIF89A_SPEC) -> Support.TOTAL
            else -> Support.NONE
        }
    }
}

