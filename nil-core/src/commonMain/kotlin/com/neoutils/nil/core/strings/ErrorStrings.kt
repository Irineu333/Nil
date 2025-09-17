package com.neoutils.nil.core.strings

import kotlin.reflect.KClass

internal data class ErrorStrings(
    val decoder: DecoderErrorStrings = DecoderErrorStrings(),
    val fetchers: FetcherErrorStrings = FetcherErrorStrings(),
    val unsupportedType: (type: KClass<*>) -> String = { type -> "Unsupported type $type" },
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
