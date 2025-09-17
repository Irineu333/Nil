@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.chain.Chain
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.extension.leveled
import com.neoutils.nil.core.extension.resolve
import com.neoutils.nil.core.foundation.InterceptorsExtras
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.core.util.Extras
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

class Nil(
    private val extras: Extras
) {
    private val interceptors = extras[InterceptorsExtras].leveled()

    fun resolve(
        request: Request
    ): Flow<PainterResource> {

        return interceptors.fold(
            flowOf(Chain(request))
        ) { flow, interceptor ->
            flow.flatMapConcat { chain ->
                interceptor.resolve(extras, chain)
            }
        }.resolve()
    }
}
