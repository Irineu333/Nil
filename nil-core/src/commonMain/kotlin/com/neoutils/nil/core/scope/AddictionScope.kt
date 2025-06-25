package com.neoutils.nil.core.scope

class AddictionScope<T>(
    private var values: MutableList<T> = mutableListOf()
) {
    fun add(value: T) = values.add(value)

    internal fun build() = values.toList()
}
