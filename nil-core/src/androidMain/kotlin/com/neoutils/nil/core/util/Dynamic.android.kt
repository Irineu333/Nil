package com.neoutils.nil.core.util

import java.util.ServiceLoader

internal actual inline fun <reified T : Any> load(): List<T> = ServiceLoader.load(
    T::class.java,
    T::class.java.classLoader,
).toList()
