package com.neoutils.nil.core.strings

internal data class ErrorStrings(
    val decoder: DecoderErrorStrings = DecoderErrorStrings(),
    val fetcher: FetcherErrorStrings = FetcherErrorStrings(),
    val notSupported: String = "Not supported",
)

internal data class DecoderErrorStrings(
    val notFound: String = "No decoder found",
    val notSupportedFound: String = "No supported decoder found",
)

internal data class FetcherErrorStrings(
    val notFound: String = "No fetcher found",
    val noRequiredFound: String = "No required fetcher found"
)
