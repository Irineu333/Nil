package com.neoutils.nil.core.exception

import com.neoutils.nil.core.strings.DecoderErrorStrings

private val error = DecoderErrorStrings()

class ResolveException(
    message: String = error.noDecodedData
) : Exception(message)
