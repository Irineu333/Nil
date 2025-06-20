package com.neoutils.nil.core.extension

import com.neoutils.nil.core.decoder.Decoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

fun Decoder.fetch(input: ByteArray) = callbackFlow {
    val painter = withContext(Dispatchers.Default) {
        decode(input)
    }

    send(painter)
    awaitCancellation()
}