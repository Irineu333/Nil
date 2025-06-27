package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.exception.NoDecoderFound
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.strings.DecoderErrorStrings
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

private val error = DecoderErrorStrings()

class DecodeInterceptor : Interceptor {
    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Chain {
        return when (val data = chain.data) {
            is Resource.Loading -> {
                chain.copy(
                    painter = PainterResource.Loading(data.progress)
                )
            }

            is Resource.Result.Failure -> {
                chain.copy(
                    painter = PainterResource.Result.Failure(data.throwable)
                )
            }

            is Resource.Result.Success<ByteArray> -> {
                chain.copy(
                    painter = settings
                        .decoderFor(data.value)
                        .toPainterResource { decoder ->
                            decoder.decode(
                                input = data.value,
                                extras = settings.extras,
                            )
                        }
                )
            }

        }
    }

    private suspend fun Settings.decoderFor(bytes: ByteArray): Resource.Result<Decoder> {

        if (decoders.isEmpty()) {
            return Resource.Result.Failure(NoDecoderFound(error.notFound))
        }

        val decoder = decoders.maxBy { it.support(bytes) }

        if (decoder.support(bytes) == Support.NONE) {
            return Resource.Result.Failure(NoDecoderFound(error.notSupportedFound))
        }

        return Resource.Result.Success(decoder)
    }
}