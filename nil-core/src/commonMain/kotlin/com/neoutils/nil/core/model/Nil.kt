@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Request
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class Nil(
    internal val settings: Settings
) {

    fun execute(request: Request): Flow<PainterResource> {
        return settings.interceptors
            .sortedBy { it.level }
            .fold(flowOf(Chain(request))) { chain, interceptor ->
                chain.flatMapMerge {
                    interceptor.intercept(settings, it)
                }
            }.map { chain ->
                chain.painter
            }
    }
}
