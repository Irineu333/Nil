package com.neoutils.nil.core.scope

@SettingsDsl
class AddictionScope<T>(
    private var values: MutableList<T> = mutableListOf()
) {
    fun add(value: T) = values.add(value)

    internal fun build() = values.toList()
}
