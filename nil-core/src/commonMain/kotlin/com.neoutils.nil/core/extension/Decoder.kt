package com.neoutils.nil.core.extension

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.exception.NoDecoderFound
import com.neoutils.nil.core.strings.DecoderErrorStrings
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

private val error = DecoderErrorStrings()

internal suspend fun List<Decoder>.decoderFor(bytes: ByteArray): Resource.Result<Decoder> {

    if (isEmpty()) {
        return Resource.Result.Failure(NoDecoderFound(error.notFound))
    }

    val decoder = maxBy { it.support(bytes) }

    if (decoder.support(bytes) == Support.NONE) {
        return Resource.Result.Failure(NoDecoderFound(error.notSupportedFound))
    }

    return Resource.Result.Success(decoder)
}
