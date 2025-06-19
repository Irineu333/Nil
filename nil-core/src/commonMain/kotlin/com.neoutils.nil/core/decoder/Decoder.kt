package com.neoutils.nil.core.decoder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.neoutils.nil.core.provider.PainterProvider
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

val LocalDecoders = compositionLocalOf<List<Decoder>> { listOf() }

interface Decoder {
    suspend fun decode(input: ByteArray): Resource.Result<PainterProvider>
    fun support(input: ByteArray): Support
}

@Composable
fun rememberTargetDecoder(
    input: ByteArray,
    decoders: List<Decoder> = LocalDecoders.current
): Decoder {

    val decodersSupport = remember(decoders, input) {
        decoders.associateWith { it.support(input) }
    }

    val decoder = remember(input, decodersSupport) {
        decodersSupport.maxByOrNull { it.value }?.key
    }

    return checkNotNull(decoder) { "No supported decoder found" }
}