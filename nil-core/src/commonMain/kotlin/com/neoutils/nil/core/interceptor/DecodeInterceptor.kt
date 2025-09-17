package com.neoutils.nil.core.interceptor

import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.extension.getOrElse
import com.neoutils.nil.core.extension.process
import com.neoutils.nil.core.extension.skip
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.usecase.GetDecoderUseCase
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource

val DecodersExtra = Extras.Key(Dynamic.decoders)

class DecodeInterceptor(
    private val decoderFor: GetDecoderUseCase = GetDecoderUseCase()
) : Interceptor(Level.PAINTER) {

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): ChainResult {

        if (chain.painter != null) return chain.skip()

        val data = chain.data ?: return chain.skip()
        val bytes = data.getOrElse { return chain.skip() }

        return when (val decoder = decoderFor(settings.extras, bytes)) {
            is Resource.Result.Failure -> {
                chain.process(
                    painter = decoder
                )
            }

            is Resource.Result.Success<Decoder> -> {
                chain.process(
                    painter = decoder.value.decode(
                        input = bytes,
                        extras = settings.extras,
                    )
                )
            }
        }
    }
}
