package com.neoutils.nil.core.scope

class ExtrasScope(
    private val builder: Extras.Builder = Extras.Builder()
) {
    fun set(key: Extras.Key<*>, value: Any) {
        builder[key] = value
    }

    internal fun build() = builder.build()
}