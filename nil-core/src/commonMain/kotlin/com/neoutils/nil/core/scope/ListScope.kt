package com.neoutils.nil.core.scope

import kotlin.reflect.KClass

@SettingsDsl
class ListScope<T : Any>(
    private var values: MutableList<T>
) {
    fun add(value: T) = values.add(value)
    fun remove(clazz: KClass<T>) = values.removeIf { it::class == clazz }

    internal fun get() = values.toList()

    companion object {
        fun <T : Any> from(values: List<T>) = ListScope(values.toMutableList())
    }
}
