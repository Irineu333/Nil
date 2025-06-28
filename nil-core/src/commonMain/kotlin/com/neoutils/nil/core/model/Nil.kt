@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Request
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class Nil(
    private val settings: Settings
) {
    fun async(request: Request): Flow<PainterResource> {

        val interceptors = Level.entries.flatMap { level ->
            settings.interceptors.filter {
                it.level.contains(level)
            }
        }

        return interceptors.fold(
            flowOf(Chain(request))
        ) { chain, interceptor ->
            chain.flatMapMerge {
                interceptor.intercept(settings, it)
            }
        }.map {
            it.painter
        }
    }
}
