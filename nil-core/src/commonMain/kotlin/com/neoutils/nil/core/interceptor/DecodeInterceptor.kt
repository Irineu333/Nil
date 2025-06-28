package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.exception.NoDecoderFound
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.strings.DecoderErrorStrings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val error = DecoderErrorStrings()

class DecodeInterceptor : Interceptor(Level.DECODE) {

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> = flowOf(
        chain.copy(
            painter = chain.data.toPainterResource { data ->
                settings
                    .decoderFor(data)
                    .toPainterResource { decoder ->
                        decoder.decode(
                            input = data,
                            extras = settings.extras,
                        )
                    }
            }
        )
    )

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