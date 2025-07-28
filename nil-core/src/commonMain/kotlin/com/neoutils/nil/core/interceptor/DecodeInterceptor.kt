package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.exception.NoDecoderFound
import com.neoutils.nil.core.extension.getOrElse
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.strings.DecoderErrorStrings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

private val error = DecoderErrorStrings()

class DecodeInterceptor : Interceptor(Level.PAINTER) {

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): ChainResult {

        if (chain.painter != null) return ChainResult.Skip

        val data = chain.data ?: return ChainResult.Skip

        val bytes = data.getOrElse { return ChainResult.Skip }

        return when(val decoder = settings.decoderFor(bytes)) {
            is Resource.Result.Failure -> {
                ChainResult.Process(
                    chain.doCopy(
                        painter = decoder
                    )
                )
            }
            is Resource.Result.Success<Decoder> -> {
                ChainResult.Process(
                    chain.doCopy(
                        painter = decoder.value.decode(
                            input = bytes,
                            extras = settings.extras,
                        )
                    )
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
