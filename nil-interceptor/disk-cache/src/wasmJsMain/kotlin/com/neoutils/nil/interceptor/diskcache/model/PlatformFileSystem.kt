package com.neoutils.nil.interceptor.diskcache.model

import okio.*
import okio.Path.Companion.toPath

// For web, we provide a minimal implementation since disk caching is not fully supported
// This issue was marked as "Aceitável" (acceptable) by the user
actual object PlatformFileSystem {
    actual val fileSystem: FileSystem = object : FileSystem() {
        override fun canonicalize(path: Path): Path = path
        override fun metadataOrNull(path: Path): FileMetadata? = null
        override fun list(dir: Path): List<Path> = emptyList()
        override fun listOrNull(dir: Path): List<Path>? = emptyList()
        override fun openReadOnly(file: Path): FileHandle = throw UnsupportedOperationException("File access not supported on web")
        override fun openReadWrite(file: Path, mustCreate: Boolean, mustExist: Boolean): FileHandle = throw UnsupportedOperationException("File access not supported on web")
        override fun source(file: Path): Source = throw UnsupportedOperationException("File access not supported on web")
        override fun sink(file: Path, mustCreate: Boolean): Sink = throw UnsupportedOperationException("File access not supported on web")
        override fun appendingSink(file: Path, mustExist: Boolean): Sink = throw UnsupportedOperationException("File access not supported on web")
        override fun createDirectory(dir: Path, mustCreate: Boolean) {}
        override fun atomicMove(source: Path, target: Path) {}
        override fun delete(path: Path, mustExist: Boolean) {}
        override fun createSymlink(source: Path, target: Path) {}
    }
    actual val temporaryDirectory: Path = "/tmp".toPath()
}