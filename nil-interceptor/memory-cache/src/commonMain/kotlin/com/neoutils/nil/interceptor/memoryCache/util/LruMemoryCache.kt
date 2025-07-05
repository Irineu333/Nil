@file:OptIn(ExperimentalTime::class)

package com.neoutils.nil.interceptor.memoryCache.util

import com.neoutils.nil.core.contract.Cache
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.interceptor.memoryCache.util.LruMemoryCache.Value
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val cache = mutableMapOf<Request, Value<PainterResource>>()

class LruMemoryCache(
    val maxSize: Int,
) : Cache<Request, PainterResource> {

    init {
        ensureSize()
    }

    override fun has(key: Request) = cache.contains(key)

    override fun set(key: Request, value: PainterResource) {
        cache[key] = Value(value)
        ensureSize()
    }

    override fun get(key: Request): PainterResource {
        return checkNotNull(cache[key]).painter
    }

    private fun ensureSize() {
        if (maxSize <= cache.size) return

        cache.entries
            .sortedBy { it.value.time }
            .subList(minOf(maxSize, cache.size), cache.size)
            .forEach {
                cache.remove(it.key)
            }
    }

    data class Value<T>(
        val painter: T,
        val time: Instant = Clock.System.now()
    )
}