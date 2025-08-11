package com.neoutils.nil.core.exception

import com.neoutils.nil.core.strings.DecoderErrorStrings

private val error = DecoderErrorStrings()

class NotSupportFormat(
    message: String = error.notSupportedFormat
) : Exception(message)