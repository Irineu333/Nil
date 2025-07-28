package com.neoutils.nil.interceptor.diskcache.impl

import com.neoutils.nil.core.contract.Cacheable
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.foundation.Interceptor3
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.diskcache.model.DiskCacheExtra
import com.neoutils.nil.interceptor.diskcache.util.LruDiskCache
import com.neoutils.nil.util.Remember
import okio.ByteString.Companion.encodeUtf8

class DiskCacheInterceptor : Interceptor3(Level.REQUEST, Level.DATA) {

    private val caches = Remember<LruDiskCache>()

    private val Request.hash
        get() = when (this) {
            is Cacheable -> {
                key.encodeUtf8().md5().hex()
            }

            else -> null
        }

    override suspend fun intercept(
        settings: Settings,
        chain: Chain
    ): Chain.Result {
        val key = chain.request.hash ?: return Chain.Result.Skip

        val extra = settings.extras[DiskCacheExtra.ExtrasKey]

        val cache = caches(extra) {
            LruDiskCache(
                fileSystem = extra.fileSystem,
                path = extra.path,
                maxSize = extra.maxSize
            )
        }

        return when (val data = chain.data) {
            is Resource.Result.Success<ByteArray> if extra.enabled -> {
                cache[key] = data.value
                Chain.Result.Skip
            }

            is Resource.Loading if extra.enabled && cache.has(key) -> {
                Chain.Result.Sync(
                    chain.doCopy(
                        data = Resource.Result.Success(cache[key])
                    )
                )
            }

            else -> Chain.Result.Skip
        }
    }
}
