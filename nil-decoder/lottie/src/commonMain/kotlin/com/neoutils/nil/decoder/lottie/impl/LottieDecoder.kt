package com.neoutils.nil.decoder.lottie.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.lottie.painter.LottieComposePainter
import io.github.alexzhirkevich.compottie.*
import okio.Buffer
import okio.ByteString
import okio.ByteString.Companion.decodeHex

private val ZIP = "504B0304".decodeHex()

@OptIn(InternalCompottieApi::class)
class LottieDecoder : Decoder {

    private val cache = mutableMapOf<ByteArray, Support>()

    override suspend fun decode(input: ByteArray): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        val spec = when {
            isDotLottie(input) -> LottieCompositionSpec.DotLottie(input)
            isJsonLottie(input) -> LottieCompositionSpec.JsonString(input.decodeToString())
            else -> return PainterResource.Result.Failure(NotSupportException())
        }

        return PainterResource.Result.Success(LottieComposePainter(spec))
    }

    override suspend fun support(input: ByteArray): Support {
        return cache.getOrPut(input) {
            when {
                isDotLottie(input) -> Support.TOTAL
                isJsonLottie(input) -> Support.TOTAL
                else -> Support.NONE
            }
        }
    }

    private suspend fun isDotLottie(bytes: ByteArray): Boolean {

        if (!bytes.isType(ZIP)) return false

        try {
            bytes.decodeToLottieComposition(LottieAnimationFormat.DotLottie)
        } catch (_: Throwable) {
            return false
        }

        return true
    }

    private suspend fun isJsonLottie(bytes: ByteArray): Boolean {

        try {
            bytes.decodeToLottieComposition(LottieAnimationFormat.Json)
        } catch (_: Throwable) {
            return false
        }

        return true
    }
}

private fun ByteArray.isType(
    signature: ByteString
) = Buffer().use {
    it.write(this)
    it.readByteString(signature.size.toLong()) == signature
}
