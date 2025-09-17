package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.extension.getOrElse
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.usecase.GetDecoderUseCase
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource

class DecodeInterceptor(
    private val decoderFor: GetDecoderUseCase = GetDecoderUseCase()
) : Interceptor(Level.PAINTER) {

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): ChainResult {

        if (chain.painter != null) return ChainResult.Skip

        val data = chain.data ?: return ChainResult.Skip

        val bytes = data.getOrElse { return ChainResult.Skip }

        return when(val decoder = decoderFor(settings.decoders, bytes)) {
            is Resource.Result.Failure -> {
                ChainResult.Process(
                    chain.copy(
                        painter = decoder
                    )
                )
            }
            is Resource.Result.Success<Decoder> -> {
                ChainResult.Process(
                    chain.copy(
                        painter = decoder.value.decode(
                            input = bytes,
                            extras = settings.extras,
                        )
                    )
                )
            }
        }
    }
}
