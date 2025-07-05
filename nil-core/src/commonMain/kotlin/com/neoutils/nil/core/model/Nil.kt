@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.util.Level
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.contract.Request
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class Nil(
    private val settings: Settings
) {
    private val interceptors = Level.entries.flatMap { level ->
        settings.interceptors.filter {
            it.levels.contains(level)
        }
    }

    fun async(request: Request): Flow<PainterResource> {

        return interceptors.fold(
            flowOf(Chain(request))
        ) { chain, interceptor ->
            chain.flatMapConcat {
                interceptor.intercept(settings, it)
            }
        }.map {
            it.painter
        }
    }
}
