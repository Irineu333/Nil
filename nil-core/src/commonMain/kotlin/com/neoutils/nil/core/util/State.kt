package com.neoutils.nil.core.util

interface State {
    val isLoading: Boolean
    val isSuccess: Boolean
    val isFailure: Boolean
}
