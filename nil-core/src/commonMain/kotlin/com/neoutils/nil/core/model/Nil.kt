@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.extension.async
import com.neoutils.nil.core.extension.sync
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Level
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

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
            flowOf(Chain.Async(request))
        ) { flow, interceptor ->
            flow.flatMapConcat { chain ->
                interceptor.async(settings, chain)
            }
        }.getAsFlow()
    }

    suspend fun sync(
        request: Request.Sync
    ): PainterResource.Result {

        return interceptors.fold(
            Chain.Sync(request)
        ) { chain, interceptor ->
            interceptor.sync(settings, chain)
        }.getAsResult()
    }
}
