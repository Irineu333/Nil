package com.neoutils.nil.fetcher.resources.impl

import com.neoutils.nil.core.extension.resourceCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Fetcher
import com.neoutils.nil.fetcher.resources.model.RequestResource
import org.jetbrains.compose.resources.getDrawableResourceBytes

class ResourcesFetcher() : Fetcher<RequestResource>(RequestResource::class) {

    override suspend fun get(
        input: RequestResource,
        extras: Extras
    ) = resourceCatching {
        getDrawableResourceBytes(
            input.environment,
            input.res
        )
    }
}