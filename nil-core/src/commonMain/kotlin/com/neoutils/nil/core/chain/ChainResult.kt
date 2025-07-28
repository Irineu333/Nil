package com.neoutils.nil.core.chain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

sealed interface ChainResult {

    data class Process(
        val flow: Flow<Chain>
    ) : ChainResult {
        constructor(chain: Chain) : this(flowOf(chain))
    }

    data object Skip : ChainResult
}
