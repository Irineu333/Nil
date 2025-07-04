package com.neoutils.nil.interceptor.diskcache.impl

import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.util.Cacheable
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.diskcache.util.LruDiskCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okio.ByteString.Companion.encodeUtf8

class DiskCacheInterceptor : Interceptor(Level.REQUEST, Level.DATA) {

    private val Request.hash
        get() = when (this) {
            is Cacheable -> {
                key.encodeUtf8().md5().hex()
            }
            else -> null
        }

    override fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        val key = chain.request.hash ?: return flowOf(chain)

        val (fileSystem, path, maxSize, enabled) = settings.extras[DiskCacheExtra.ExtrasKey]

        if (!enabled) return flowOf(chain)

        val cache = LruDiskCache(
            fileSystem = fileSystem,
            path = path,
            maxSize = maxSize
        )

        return flowOf(
            when (val data = chain.data) {
                is Resource.Loading if cache.has(key) -> {
                    chain.copy(
                        data = Resource.Result.Success(cache[key])
                    )
                }

                is Resource.Result.Success<ByteArray> -> {
                    cache[key] = data.value
                    chain
                }

                else -> chain
            }
        )
    }
}
