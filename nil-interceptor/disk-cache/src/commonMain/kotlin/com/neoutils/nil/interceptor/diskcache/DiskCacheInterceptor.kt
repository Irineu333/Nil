package com.neoutils.nil.interceptor.diskcache

import com.neoutils.nil.core.extension.onSuccess
import com.neoutils.nil.core.model.Chain
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.source.Interceptor
import com.neoutils.nil.core.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import okio.FileSystem

private const val DefaultCachePath = "nil-cache"

val DiskCacheExtrasKey = Extras.Key(
    default = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / DefaultCachePath
)

class DiskCacheInterceptor : Interceptor(Level.REQUEST, Level.DATA) {

    private val fileSystem = FileSystem.SYSTEM

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

        val path = settings.extras[DiskCacheExtrasKey]

        val file = path / key.clear()

        if (fileSystem.exists(file)) {
            return flow {
                val bytes = fileSystem.read(file) {
                    readByteArray()
                }

                emit(
                    chain.copy(
                        data = Resource.Result.Success(bytes)
                    )
                )
            }
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
