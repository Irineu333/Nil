package com.neoutils.nil.core.scope

import kotlin.collections.plus

class ListScope<T : Any>(
    internal var values: List<T> = listOf()
) {
    fun add(value: T) {
        values += value
    }

    operator fun T.unaryPlus() {
        values += this
    }
}
