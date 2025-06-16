package core.cache

import core.util.RawImage

private val cache = mutableMapOf<String, RawImage>()

data class MemoryCache(
    val key: String,
) {
    fun get() = cache[key]

    fun set(image: RawImage) {
        cache[key] = image
    }

    companion object {
        val Disabled: MemoryCache? = null
    }
}