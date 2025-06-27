package com.neoutils.nil.fetcher.resources.impl

import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.fetcher.resources.model.RequestResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getDrawableResourceBytes

class ResourcesFetcher() : Fetcher<RequestResource>(RequestResource::class) {

    override suspend fun get(
        input: RequestResource,
        extras: Extras
    ) = runCatching {
        withContext(Dispatchers.IO) {
            getDrawableResourceBytes(input.environment, input.res)
        }
    }.map { bytes ->
        Resource.Result.Success(bytes)
    }.getOrElse {
        Resource.Result.Failure(it)
    }

    override fun fetch(
        input: RequestResource,
        extras: Extras
    ) = callbackFlow {
        runCatching {
            withContext(Dispatchers.IO) {
                getDrawableResourceBytes(input.environment, input.res)
            }
        }.onSuccess { bytes ->
            withContext(Dispatchers.Main) {
                send(Resource.Result.Success(value = bytes))
            }
        }.onFailure {
            withContext(Dispatchers.Main) {
                send(Resource.Result.Failure(it))
            }
        }

        awaitClose()
    }
}