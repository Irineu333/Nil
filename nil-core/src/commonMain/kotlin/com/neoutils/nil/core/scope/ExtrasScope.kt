package com.neoutils.nil.core.scope

import com.neoutils.nil.core.util.Extras

class ExtrasScope(val extras: Extras.Builder) {
    internal fun build() = extras.build()
}
