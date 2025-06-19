package com.neoutils.nil.decoder.gif.impl

import android.os.Build
import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toResource
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.extension.startsWith
import com.neoutils.nil.decoder.gif.format.GIF87A_SPEC
import com.neoutils.nil.decoder.gif.format.GIF89A_SPEC
import com.neoutils.nil.decoder.gif.provider.GifPainterLegacyProvider
import com.neoutils.nil.decoder.gif.provider.GitPainterApi28Provider

actual class GifDecoder : Decoder {
    actual override suspend fun decode(input: ByteArray): Resource.Result<PainterProvider> {

        if (support(input) == Support.NONE) {
            return Resource.Result.Failure(NotSupportException())
        }

        return runCatching {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                    GitPainterApi28Provider(input)
                }

                else -> {
                    GifPainterLegacyProvider(input)
                }
            }
        }.toResource()
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

