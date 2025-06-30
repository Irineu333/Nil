package com.neoutils.nil.core.scope

@SettingsDsl
class ExtrasScope(val extras: Extras.Builder) {
    internal fun build() = extras.build()
}
