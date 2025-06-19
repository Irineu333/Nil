package com.neoutils.nil.decoder.bitmap.format

// https://en.wikipedia.org/wiki/List_of_file_signatures
private val PNG_RFC_2083 = signature(0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
private val JPEG_ITU_T81 = signature(0xFF, 0xD8, 0xFF)
private val GIF87A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x37, 0x61)
private val GIF89A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x39, 0x61)
private val WEBP_RIFF = signature(0x52, 0x49, 0x46, 0x46)

internal enum class ImageFormat(
    private val signatures: List<ByteArray>
) {
    PNG(listOf(PNG_RFC_2083)),
    JPEG(listOf(JPEG_ITU_T81)),
    GIF(listOf(GIF87A_SPEC, GIF89A_SPEC)),
    WEBP(listOf(WEBP_RIFF));

    fun matches(bytes: ByteArray) = signatures.any { bytes.startsWith(it) }

    companion object {
        fun detect(bytes: ByteArray): ImageFormat? {
            return ImageFormat.entries.find { it.matches(bytes) }
        }
    }
}

private fun ByteArray.startsWith(bytes: ByteArray): Boolean {
    if (this.size < bytes.size) return false
    return bytes.indices.all { i -> this[i] == bytes[i] }
}

private fun signature(vararg bytes: Int) = bytes.map { it.toByte() }.toByteArray()