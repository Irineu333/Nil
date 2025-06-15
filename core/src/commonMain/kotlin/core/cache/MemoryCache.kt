package core.cache

import core.util.RawImage

private val cache = mutableMapOf<String, RawImage>()

class MemoryCache(
    val key: String = "",
    val enabled: Boolean = true
) {
    fun get() = cache[key].takeIf { enabled }

    fun set(image: RawImage) {
        if (enabled) {
            cache[key] = image
        }
    }
}