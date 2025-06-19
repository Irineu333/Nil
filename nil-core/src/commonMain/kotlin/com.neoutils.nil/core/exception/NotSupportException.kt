package com.neoutils.nil.core.exception

class NotSupportException(
    override val message: String = "Doesn't support"
) : Exception()