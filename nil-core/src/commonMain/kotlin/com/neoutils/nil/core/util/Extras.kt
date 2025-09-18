package com.neoutils.nil.core.util

import com.neoutils.nil.core.strings.ExtraErrorStrings
import com.neoutils.nil.core.util.Extras.Key

private val error = ExtraErrorStrings()

class Extras private constructor(
    private val extras: Map<Key<*>, Any>
) {
    operator fun <T> get(key: Key<T>): T {
        return extras.getOrDefault(key)
    }

    fun newBuilder() = Builder(extras.toMutableMap())

    class Builder(
        private val extras: MutableMap<Key<*>, Any> = mutableMapOf()
    ) {

        operator fun <T> set(key: Key<T>, value: T) {
            extras[key] = value ?: return
        }

        fun <T> update(
            key: Key<T>,
            block: (T) -> T
        ) {
            set(key, block(extras.getOrDefault(key)))
        }

        fun build(): Extras {
            return Extras(extras.toMap())
        }
    }

    class Key<T>(val default: T? = null)

    companion object {
        val EMPTY = Builder().build()
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> Map<Key<*>, Any>.getOrDefault(key: Key<T>): T {
    val value = get(key) as T ?: key.default
    return checkNotNull(value) { error.notDefined }
}
