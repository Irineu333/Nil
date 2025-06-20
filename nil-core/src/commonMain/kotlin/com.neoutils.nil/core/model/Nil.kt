@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.extension.combine
import com.neoutils.nil.core.extension.decoderFor
import com.neoutils.nil.core.extension.fetcherFor
import com.neoutils.nil.core.fetcher.Fetcher
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

class Nil(
    internal val settings: Settings
) {
    fun execute(input: Input): Flow<Resource<NilPainter>> {
        return flow {
            when (val fetcher = settings.fetchers.fetcherFor(input)) {
                is Resource.Result.Failure -> emit(fetcher)
                is Resource.Result.Success<Fetcher<Input>> -> {
                    emitAll(fetcher.data.fetch(input))
                }
            }
        }.mapLatest { dataRes ->
            dataRes.combine { data ->
                withContext(Dispatchers.Default) {
                    settings.decoders
                        .decoderFor(data)
                        .combine { decoder ->
                            decoder.decode(data)
                        }
                }
            }
        }
    }
}