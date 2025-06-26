package com.neoutils.nil.core.strings


internal data class DecoderErrorStrings(
    val notFound: String = "No decoder found",
    val notSupportedFound: String = "No supported decoder found",
    val notSupportedFormat: String = "No supported format",
)

internal data class FetcherErrorStrings(
    val notFound: String = "No fetcher found",
    val noRequiredFound: String = "No required fetcher found"
)

internal data class ExtraErrorStrings(
    val notDefined: String = "Extra key not defined"
)