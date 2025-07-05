package com.neoutils.nil.fetcher.resources.impl

import com.neoutils.nil.core.extension.toResource
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.fetcher.resources.model.RequestResource
import kotlinx.coroutines.flow.flow
import org.jetbrains.compose.resources.getDrawableResourceBytes

class ResourcesFetcher() : Fetcher<RequestResource>(RequestResource::class) {

    override fun fetch(
        input: RequestResource,
        extras: Extras
    ) = flow {
        emit(
            runCatching {
                getDrawableResourceBytes(
                    input.environment,
                    input.res
                )
            }.toResource()
        )
    }
}