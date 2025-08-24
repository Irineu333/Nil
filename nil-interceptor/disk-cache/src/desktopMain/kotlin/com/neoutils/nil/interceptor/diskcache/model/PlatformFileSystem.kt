package com.neoutils.nil.interceptor.diskcache.model

import okio.FileSystem
import okio.Path
import okio.SYSTEM

actual object PlatformFileSystem {
    actual val fileSystem: FileSystem = FileSystem.SYSTEM
    actual val temporaryDirectory: Path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
}