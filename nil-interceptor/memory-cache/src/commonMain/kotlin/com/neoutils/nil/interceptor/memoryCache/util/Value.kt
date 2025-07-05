@file:OptIn(ExperimentalTime::class)

package com.neoutils.nil.interceptor.memoryCache.util

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Value<T>(
    val painter: T,
    val time: Instant = Clock.System.now()
)