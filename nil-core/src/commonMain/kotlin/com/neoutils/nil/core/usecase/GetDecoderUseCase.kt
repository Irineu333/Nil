package com.neoutils.nil.core.usecase

import com.neoutils.nil.core.exception.NoDecoderFound
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.foundation.DecodersExtra
import com.neoutils.nil.core.strings.DecoderErrorStrings
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

private val error = DecoderErrorStrings()

class GetDecoderUseCase {
    suspend operator fun invoke(
        extras: Extras,
        bytes: ByteArray
    ): Resource.Result<Decoder> {

        val decoders = extras[DecodersExtra]

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