package com.neoutils.nil.interceptor.diskcache.extension

import com.neoutils.nil.interceptor.diskcache.util.SizeUnit

val Int.gb get() = SizeUnit.GB(this)
val Int.mb get() = SizeUnit.MB(this)
val Int.kb get() = SizeUnit.KB(this)

fun SizeUnit.toBytes() = when (this) {
    is SizeUnit.GB -> 1024 * 1024 * 1024 * value
    is SizeUnit.MB -> 1024 * 1024 * value
    is SizeUnit.KB -> 1024 * value
}
