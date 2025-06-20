package com.neoutils.nil.core.extension

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

suspend fun List<Decoder>.getFrom(
    input: ByteArray
): Resource.Result<Decoder> {

    if (isEmpty()) {
        return Resource.Result.Failure(NoDecoderFound())
    }

    val decoder = maxBy { it.support(input) }

    if (decoder.support(input) == Support.NONE) {
        return Resource.Result.Failure(NoDecoderFound("No supported decoder found"))
    }

    return Resource.Result.Success(decoder)
}

class NoDecoderFound(
    message: String = "No decoder found"
) : Exception(message)