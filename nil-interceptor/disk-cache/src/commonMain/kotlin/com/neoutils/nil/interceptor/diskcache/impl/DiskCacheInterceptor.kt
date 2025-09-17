package com.neoutils.nil.interceptor.diskcache.impl

import com.neoutils.nil.core.contract.Cacheable
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.extension.getOrElse
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.chain.ChainResult
import com.neoutils.nil.core.extension.process
import com.neoutils.nil.core.extension.skip
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.diskcache.model.DiskCacheExtra
import com.neoutils.nil.interceptor.diskcache.util.LruDiskCache
import com.neoutils.nil.util.Remember
import okio.ByteString.Companion.encodeUtf8

class DiskCacheInterceptor : Interceptor(Level.REQUEST, Level.DATA) {

    private val remember = Remember<LruDiskCache>()

    private val Request.hash
        get() = when (this) {
            is Cacheable -> {
                key.encodeUtf8().md5().hex()
            }

            else -> null
        }

    override suspend fun intercept(
        extras: Extras,
        chain: Chain
    ): ChainResult {
        val key = chain.request.hash ?: return chain.skip()

        val config = extras[DiskCacheExtra.ExtrasKey]

        if (!config.enabled) return chain.skip()

        val cache = remember(config) {
            LruDiskCache(
                fileSystem = config.fileSystem,
                path = config.path,
                maxSize = config.maxSize
            )
        }

        if (chain.data == null && cache.has(key)) {

            return chain.process(
                data = Resource.Result.Success(cache[key])
            )
        }

        val data = chain.data ?: return chain.skip()

        cache[key] = data.getOrElse { return chain.skip() }

        return chain.skip()
    }
}
