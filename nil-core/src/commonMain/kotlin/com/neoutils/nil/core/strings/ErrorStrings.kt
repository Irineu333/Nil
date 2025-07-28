package com.neoutils.nil.core.strings

internal data class ErrorStrings(
    val decoder: DecoderErrorStrings = DecoderErrorStrings(),
    val fetchers: FetcherErrorStrings = FetcherErrorStrings()
)

internal data class DecoderErrorStrings(
    val notFound: String = "No decoder found",
    val notSupportedFound: String = "No supported decoder found",
    val notSupportedFormat: String = "No supported format",
    val noDecodedData: String = "Unencoded data"
)

internal data class FetcherErrorStrings(
    val notFound: String = "No fetcher found",
    val noRequiredFound: String = "No required fetcher found",
    val noData: String = "No data"
)

internal data class ExtraErrorStrings(
    val notDefined: String = "Extra key not defined"
)
