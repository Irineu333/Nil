package com.neoutils.nil.core.exception

import com.neoutils.nil.core.strings.ErrorStrings

private val error = ErrorStrings()

class NotSupportException(
    message: String = error.notSupported
) : Exception(message)