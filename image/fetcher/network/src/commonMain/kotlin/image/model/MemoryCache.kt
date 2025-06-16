package image.model

private val cache = mutableMapOf<String, ByteArray>()

data class MemoryCache(
    val key: String,
) {
    fun get() = cache[key]

    fun set(image: ByteArray) {
        cache[key] = image
    }

    companion object {
        val Disabled: MemoryCache? = null
    }
}