package com.neoutils.nil.decoder.lottie.impl

import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.extension.toPainterResource
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.lottie.model.LottieParams
import com.neoutils.nil.decoder.lottie.painter.LottieComposePainter
import com.neoutils.nil.type.Type
import io.github.alexzhirkevich.compottie.*
import kotlinx.coroutines.runBlocking

@OptIn(InternalCompottieApi::class)
class LottieDecoder : Decoder<LottieParams> {

    override val paramType = LottieParams::class

    private val cache = mutableMapOf<ByteArray, Support>()

    override suspend fun decode(
        input: ByteArray,
        param: LottieParams?
    ): PainterResource.Result {

        if (support(input) == Support.NONE) {
            return PainterResource.Result.Failure(NotSupportException())
        }

        val spec = when {
            isDotLottie(input) -> LottieCompositionSpec.DotLottie(input)
            isJsonLottie(input) -> LottieCompositionSpec.JsonString(input.decodeToString())
            else -> return PainterResource.Result.Failure(NotSupportException())
        }

        return runCatching {
            LottieComposePainter(
                composition = spec.load(),
                iterations = param?.iterations,
                speed = param?.speed
            )
        }.toPainterResource()
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

        if (!Type.ZIP.matches(bytes)) return false

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
