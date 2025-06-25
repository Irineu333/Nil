package com.neoutils.nil.core.scope

import kotlin.collections.iterator

class Extras private constructor(
    private val extras: Map<Key<*>, Any>
) {

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: Key<T>): T? {
        return extras[key] as T ?: key.default
    }

    class Builder {

        private val extras: MutableMap<Key<*>, Any> = mutableMapOf()

        operator fun <T> set(key: Key<T>, value: T) {
            extras[key] = value ?: return
        }

        fun add(extras: Extras) {
            for ((key, value) in extras.extras) {
                set(key, value)
            }
        }

        fun build(): Extras {
            return Extras(extras.toMap())
        }
    }

    class Key<out T>(val default: T)
}