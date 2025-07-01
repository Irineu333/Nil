package com.neoutils.nil.core.scope

import com.neoutils.nil.core.annotation.SettingsDsl
import com.neoutils.nil.core.util.Extras

@SettingsDsl
class ExtrasScope(val extras: Extras.Builder) {
    internal fun build() = extras.build()
}
