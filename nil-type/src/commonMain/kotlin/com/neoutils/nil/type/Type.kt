package com.neoutils.nil.type

import okio.Buffer
import okio.ByteString
import okio.ByteString.Companion.decodeHex
import okio.use

// https://en.wikipedia.org/wiki/List_of_file_signatures
private val PNG_SIGN = "89504E470D0A1A0A".decodeHex()
private val JPEG_SIGN = "FFD8FF".decodeHex()
private val GIF87A_SIGN = "474946383761".decodeHex()
private val GIF89A_SIGN = "474946383961".decodeHex()
private val WEBP_RIFF_SIGN = "52494646".decodeHex()
private val ZIP_SIGN = "504B0304".decodeHex()

enum class Type(private vararg val sign: ByteString) {
    PNG(PNG_SIGN),
    JPEG(JPEG_SIGN),
    GIF(GIF87A_SIGN, GIF89A_SIGN),
    WEBP(WEBP_RIFF_SIGN),
    ZIP(ZIP_SIGN);

    fun matches(bytes: ByteArray) = sign.any { bytes.matches(it) }

    companion object {
        fun detect(bytes: ByteArray): Type? {
            return entries.find { it.matches(bytes) }
        }
    }
}

private fun ByteArray.matches(
    signature: ByteString
) = let { data ->
    Buffer().use {
        it.write(data)
        it.rangeEquals(0, signature)
    }
}