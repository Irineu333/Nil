package com.neoutils.nil.interceptor.diskcache.extension

import com.neoutils.nil.core.util.Extras
import okio.FileSystem
import okio.Path

private const val DEFAULT_CACHE_PATH = "nil-cache"

class DiskCacheExtra(
    val fileSystem: FileSystem = FileSystem.SYSTEM,
    val path: Path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / DEFAULT_CACHE_PATH,
    val enabled: Boolean = true,
) {

    operator fun component1() = fileSystem
    operator fun component2() = path
    operator fun component3() = enabled

    fun newBuilder() = Builder(
        fileSystem = fileSystem,
        path = path,
        enabled = enabled
    )

    class Builder(
        var fileSystem: FileSystem,
        var path: Path,
        var enabled: Boolean,
    ) {
        internal fun build() = DiskCacheExtra(
            fileSystem = fileSystem,
            path = path,
            enabled = enabled,
        )
    }

    companion object {
        val ExtrasKey = Extras.Key(DiskCacheExtra())
    }
}
