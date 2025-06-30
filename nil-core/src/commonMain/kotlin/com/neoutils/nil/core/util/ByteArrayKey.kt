package com.neoutils.nil.core.util

data class ByteArrayKey(val bytes: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ByteArrayKey) return false

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}

val ByteArray.key get() = ByteArrayKey(this)