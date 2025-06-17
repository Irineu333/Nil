package image.fetcher.resources.impl

import image.core.fetcher.Fetcher
import image.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getDrawableResourceBytes

class ResourcesFetcher(
    private val environment: ResourceEnvironment
) : Fetcher<DrawableResource> {

    override suspend fun get(
        input: DrawableResource
    ) = runCatching {
        withContext(Dispatchers.IO) {
            getDrawableResourceBytes(environment, input)
        }
    }.map { bytes ->
        Resource.Result.Success(bytes)
    }.getOrElse {
        Resource.Result.Failure(it)
    }

    override fun fetch(input: DrawableResource) = callbackFlow {
        runCatching {
            withContext(Dispatchers.IO) {
                getDrawableResourceBytes(environment, input)
            }
        }.onSuccess { bytes ->
            withContext(Dispatchers.Main) {
                send(Resource.Result.Success(data = bytes))
            }
        }.onFailure {
            withContext(Dispatchers.Main) {
                send(Resource.Result.Failure(it))
            }
        }

        awaitClose()
    }
}