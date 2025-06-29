package com.neoutils.nil.core.util

import com.neoutils.nil.core.source.Decoder
import java.util.*

object Dynamic {
    val decoders = load<Decoder>()
}

private inline fun <reified T : Any> load(): List<T> = ServiceLoader.load(
    T::class.java,
    T::class.java.classLoader,
).iterator().asSequence().toList()