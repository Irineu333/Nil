package image.decoder.gif.extension

internal fun ByteArray.startsWith(bytes: ByteArray): Boolean {
    if (this.size < bytes.size) return false
    return bytes.indices.all { i -> this[i] == bytes[i] }
}
