package com.neoutils.nil.core.chain

import kotlinx.coroutines.flow.Flow

sealed interface ChainResult {

    data class Stream(
        val chain: Flow<Chain>
    ) : ChainResult

    data class Single(
        val chain: Chain
    ) : ChainResult
}
