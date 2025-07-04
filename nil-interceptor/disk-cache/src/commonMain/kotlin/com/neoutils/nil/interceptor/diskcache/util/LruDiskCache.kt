package com.neoutils.nil.interceptor.diskcache.util

import com.neoutils.nil.interceptor.diskcache.extension.toBytes
import com.neoutils.nil.core.contract.Cache
import okio.FileSystem
import okio.Path

class LruDiskCache(
    private val fileSystem: FileSystem,
    private val path: Path,
    private val maxSize: SizeUnit
) : Cache<String, ByteArray> {

    init {
        fileSystem.createDirectories(path)
    }

    private var files = mutableMapOf(
        pairs = fileSystem.list(path).map {
            Pair(it, fileSystem.metadata(it))
        }.toTypedArray()
    )

    init {
        ensureSize()
    }

    override fun has(key: String): Boolean {
        val file = path / key
        return fileSystem.exists(file)
    }

    override operator fun set(key: String, value: ByteArray) {
        val file = path / key

        fileSystem.write(file) {
            write(value)
        }

        files[file] = fileSystem.metadata(file)
        ensureSize()
    }

    override operator fun get(key: String): ByteArray {
        val file = path / key

        // Update last modified time
        fileSystem.atomicMove(file, file)

        files[file] = fileSystem.metadata(file)

        return fileSystem.read(file) {
            readByteArray()
        }
    }

    private fun ensureSize() {
        var size = files.values
            .sortedBy { it.lastModifiedAtMillis }
            .sumOf { metadata -> metadata.size ?: 0L }

        for ((file, metadata) in files) {
            if (size > maxSize.toBytes()) {
                fileSystem.delete(file)
                size -= metadata.size ?: 0L
                files.remove(file)
            } else {
                break
            }
        }
    }
}
