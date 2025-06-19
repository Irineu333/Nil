package com.neoutils.nil.core.decoder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Support

val LocalDecoders = compositionLocalOf<List<Decoder>> { listOf() }

interface Decoder {
    fun decode(input: ByteArray): PainterProvider
    fun support(input: ByteArray): Support
}

@Composable
fun rememberTargetDecoder(
    input: ByteArray,
    decoders: List<Decoder> = LocalDecoders.current
): Decoder {

    val supportedDecoders = remember(decoders, input) { decoders.filter { it.support(input) != Support.NONE } }

    val decoder = remember(input, supportedDecoders) { supportedDecoders.maxByOrNull { it.support(input) } }

    return checkNotNull(decoder) { "No supported decoder found" }
}