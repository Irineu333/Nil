package com.neoutils.nil.interceptor.diskcache.impl

import com.neoutils.nil.core.contract.Cacheable
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.foundation.Interceptor
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.diskcache.model.DiskCacheExtra
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

        val cache = settings.extras[DiskCacheExtra.ExtrasKey]

        return flowOf(
            when (val data = chain.data) {
                is Resource.Result.Success<ByteArray> if cache.enabled -> {
                    cache[key] = data.value
                    chain
                }

                is Resource.Loading if cache.enabled && cache.has(key) -> {
                    chain.copy(
                        data = Resource.Result.Success(cache[key])
                    )
                }

                else -> chain
            }
        )
    }
}
