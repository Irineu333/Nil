package com.neoutils.nil.decoder.lottie.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.lottie.provider.LottiePainterProvider
import io.github.alexzhirkevich.compottie.*
import kotlinx.coroutines.runBlocking

@OptIn(InternalCompottieApi::class)
class LottieDecoder : Decoder {

    private val cache = mutableMapOf<ByteArray, Support>()

    override fun decode(input: ByteArray): PainterProvider {

        val spec = runBlocking {
            when {
                isDotLottie(input) -> LottieCompositionSpec.DotLottie(input)
                isJsonLottie(input) -> LottieCompositionSpec.JsonString(input.decodeToString())
                else -> error("Doesn't support")
            }
        }

        return LottiePainterProvider(spec)
    }

    override fun support(input: ByteArray): Support {
        return runBlocking {
            cache.getOrPut(input) {
                when {
                    isDotLottie(input) -> Support.TOTAL
                    isJsonLottie(input) -> Support.TOTAL
                    else -> Support.NONE
                }
            }
        }
    }

    private suspend fun isDotLottie(bytes: ByteArray): Boolean {

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
