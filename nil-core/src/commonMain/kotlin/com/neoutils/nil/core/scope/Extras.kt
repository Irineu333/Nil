package com.neoutils.nil.core.scope

import androidx.compose.runtime.compositionLocalOf
import com.neoutils.nil.core.scope.Extras.Key
import com.neoutils.nil.core.strings.ExtraErrorStrings

val LocalExtras = compositionLocalOf { Extras.EMPTY }

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

        @Suppress("UNCHECKED_CAST")
        fun add(extras: Extras) {
            for ((key, value) in extras.extras) {
                set(key as Key<Any>, value)
            }
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
