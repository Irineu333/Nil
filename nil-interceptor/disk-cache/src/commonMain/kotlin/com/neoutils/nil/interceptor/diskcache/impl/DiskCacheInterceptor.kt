package com.neoutils.nil.interceptor.diskcache.impl

import com.neoutils.nil.core.extension.onSuccess
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.util.Cacheable
import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.interceptor.diskcache.extension.DiskCacheExtra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DiskCacheInterceptor : Interceptor(Level.REQUEST, Level.DATA) {

    private val Request.key
        get() = when (this) {
            is Cacheable -> key
            else -> null
        }

    override fun intercept(
        settings: Settings,
        chain: Chain
    ): Flow<Chain> {

        val key = chain.request.key ?: return flowOf(chain)

        val (fileSystem, path, enabled) = settings.extras[DiskCacheExtra.ExtrasKey]

        if (!enabled) return flowOf(chain)

        val file = path / key.clear()

        if (fileSystem.exists(file)) {

            val bytes = fileSystem.read(file) {
                readByteArray()
            }

            return flowOf(
                chain.copy(
                    data = Resource.Result.Success(bytes)
                )
            )
        }

        chain.data.onSuccess { chain ->
            fileSystem.createDirectories(path)
            fileSystem.write(file) {
                write(chain)
            }
        }

        return flowOf(chain)
    }

    private fun String.clear() = replace(
        regex = Regex("[^a-zA-Z0-9_.-]"),
        replacement = "_"
    ).take(255)
}
