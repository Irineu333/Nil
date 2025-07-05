package com.neoutils.nil.util

class Remember<T : Any> {

    private var value: T? = null
    private var keys: Array<out Any>? = null

    operator fun invoke(vararg keys: Any, block: () -> T): T {

        if (this.keys.contentDeepEquals(keys)) {
            return value ?: block().also {
                value = it
            }
        }

        this.keys = keys

        return block().also {
            value = it
        }
    }
}