package com.neoutils.nil.interceptor.diskcache

import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Request
import okio.FileSystem
import okio.Path


internal fun Request.toKey(): String {
    return toString()
        .replace(Regex("[^a-zA-Z0-9_.-]"), "_")
        .take(255)
}
