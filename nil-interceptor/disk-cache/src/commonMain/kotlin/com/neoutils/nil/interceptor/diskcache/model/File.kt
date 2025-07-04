package com.neoutils.nil.interceptor.diskcache.model

import okio.FileMetadata
import okio.Path

data class File(
    val path: Path,
    val metadata: FileMetadata
)
