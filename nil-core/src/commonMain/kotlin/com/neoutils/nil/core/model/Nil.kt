@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Request
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Nil(
    internal val settings: Settings
) {

    fun execute(request: Request): Flow<PainterResource> {
        return flow {

            val chain = settings.interceptors.fold(
                Chain(request)
            ) { chain, interceptor ->
                interceptor.intercept(settings, chain)
            }

            emit(chain.painter)
        }
    }
}
