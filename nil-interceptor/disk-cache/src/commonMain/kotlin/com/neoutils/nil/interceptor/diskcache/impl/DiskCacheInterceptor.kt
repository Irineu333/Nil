package com.neoutils.nil.interceptor.diskcache.impl

import com.neoutils.nil.core.contract.Cacheable
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.extension.getOrElse
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.diskcache.model.DiskCacheExtra
import com.neoutils.nil.interceptor.diskcache.util.LruDiskCache
import com.neoutils.nil.util.Remember
import okio.ByteString.Companion.encodeUtf8

class DiskCacheInterceptor : Interceptor(Level.REQUEST, Level.DATA) {

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
    ): ChainResult {
        val key = chain.request.hash ?: return ChainResult.Skip

        val extra = settings.extras[DiskCacheExtra.ExtrasKey]

        if (!extra.enabled) return ChainResult.Skip

        val cache = caches(extra) {
            LruDiskCache(
                fileSystem = extra.fileSystem,
                path = extra.path,
                maxSize = extra.maxSize
            )
        }

        if (chain.data == null && cache.has(key)) {

            return ChainResult.Process(
                chain.doCopy(
                    data = Resource.Result.Success(cache[key])
                )
            )
        }

        val data = chain.data ?: return ChainResult.Skip

        cache[key] = data.getOrElse { return ChainResult.Skip }

        return ChainResult.Skip
    }
}
