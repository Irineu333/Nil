package image.decoder.gif.util

fun signature(vararg bytes: Int) = bytes.map { it.toByte() }.toByteArray()