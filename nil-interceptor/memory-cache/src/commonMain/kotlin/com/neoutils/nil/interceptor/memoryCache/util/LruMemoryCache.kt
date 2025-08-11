@file:OptIn(ExperimentalTime::class)

package com.neoutils.nil.interceptor.memoryCache.util

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.contract.Cache
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Resource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class LruMemoryCache(
    private val maxSize: Int,
) : Cache<Request, Painter> {

    private val cache = mutableMapOf<Request, ValueWithTime<Painter>>()

    init {
        ensureSize()
    }

    override fun has(key: Request) = cache.contains(key)

    override fun set(key: Request, value: Painter) {
        cache[key] = ValueWithTime(value)
        ensureSize()
    }

    override fun get(key: Request): Painter {
        return checkNotNull(cache[key]).also {
            cache[key] = it.copy(time = Clock.System.now())
        }.painter
    }

    private fun ensureSize() {
        if (maxSize <= cache.size) return

        cache.entries
            .sortedBy { it.value.time }
            .subList(maxSize.coerceAtMost(cache.size), cache.size)
            .forEach {
                cache.remove(it.key)
            }
    }
}

private data class ValueWithTime<T>(
    val painter: T,
    val time: Instant = Clock.System.now()
)