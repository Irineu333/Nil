package com.neoutils.nil.interceptor.diskcache.util

import com.neoutils.nil.interceptor.diskcache.extension.toBytes
import com.neoutils.nil.interceptor.diskcache.model.File
import okio.FileSystem
import okio.Path

class LruDiskCache(
    private val fileSystem: FileSystem,
    private val path: Path,
    private val maxSize: SizeUnit
) {

    init {
        fileSystem.createDirectories(path)
        ensureSize()
    }

    fun has(key: String): Boolean {
        val file = path / key
        return fileSystem.exists(file)
    }

    operator fun set(key: String, value: ByteArray) {
        val file = path / key

        fileSystem.write(file) {
            write(value)
        }

        ensureSize()
    }

    operator fun get(key: String): ByteArray {
        val file = path / key

        // Update last modified time
        fileSystem.atomicMove(file, file)

        return fileSystem.read(file) {
            readByteArray()
        }
    }

    private fun ensureSize() {

        val files = fileSystem.list(path)
            .map { file -> File(file, fileSystem.metadata(file)) }
            .sortedBy { it.metadata.lastModifiedAtMillis }

        var size = files.sumOf { it.metadata.size ?: 0L }

        for ((file, metadata) in files) {
            if (size > maxSize.toBytes()) {
                fileSystem.delete(file)
                size -= metadata.size ?: 0L
            } else {
                break
            }
        }
    }
}
