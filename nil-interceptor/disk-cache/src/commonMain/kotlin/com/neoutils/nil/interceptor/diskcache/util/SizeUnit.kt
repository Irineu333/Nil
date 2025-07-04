package com.neoutils.nil.interceptor.diskcache.util

sealed interface SizeUnit {

    val value: Int

    data class GB(
        override val value: Int
    ) : SizeUnit

    data class MB(
        override val value: Int
    ) : SizeUnit

    data class KB(
        override val value: Int
    ) : SizeUnit
}
