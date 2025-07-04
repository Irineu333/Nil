package com.neoutils.nil.interceptor.diskcache.impl

import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.interceptor.diskcache.extension.mb
import com.neoutils.nil.interceptor.diskcache.util.LruDiskCache
import com.neoutils.nil.interceptor.diskcache.util.SizeUnit
import okio.FileSystem
import okio.Path

private const val DEFAULT_CACHE_PATH = "nil-cache"

class DiskCacheExtra(
    val fileSystem: FileSystem = FileSystem.SYSTEM,
    val path: Path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / DEFAULT_CACHE_PATH,
    val maxSize: SizeUnit = 100.mb,
    val enabled: Boolean = true,
) {
    val cache by lazy {
        LruDiskCache(
            fileSystem = fileSystem,
            path = path,
            maxSize = maxSize
        )
    }

    fun newBuilder() = Builder(
        fileSystem = fileSystem,
        path = path,
        enabled = enabled,
        maxSize = maxSize
    )

    class Builder(
        var fileSystem: FileSystem,
        var path: Path,
        var enabled: Boolean,
        var maxSize: SizeUnit,
    ) {
        internal fun build() = DiskCacheExtra(
            fileSystem = fileSystem,
            path = path,
            enabled = enabled,
            maxSize = maxSize,
        )
    }

    companion object {
        val ExtrasKey = Extras.Key(DiskCacheExtra())
    }
}
