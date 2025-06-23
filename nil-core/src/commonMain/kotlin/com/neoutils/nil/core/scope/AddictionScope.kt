package com.neoutils.nil.core.scope

class AddictionScope<T>(
    internal var values: List<T> = listOf()
) {
    fun add(value: T) {
        values += value
    }
}