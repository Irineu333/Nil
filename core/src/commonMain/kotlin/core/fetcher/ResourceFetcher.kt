package core.fetcher

import core.extension.toRawImage
import core.util.RawImage
import core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getDrawableResourceBytes

class ResourceFetcher(
    val environment: ResourceEnvironment
) : Fetcher<DrawableResource> {
    override suspend fun get(
        input: DrawableResource
    ): Resource.Result<RawImage> {

        return runCatching {
            withContext(Dispatchers.IO) {
                getDrawableResourceBytes(environment, input)
            }
        }.map { bytes ->
            withContext(Dispatchers.Default) {
                bytes.toRawImage()
            }
        }.map { rawImage ->
            Resource.Result.Success(rawImage)
        }.getOrElse {
            Resource.Result.Failure(it)
        }
    }

    override fun fetch(input: DrawableResource) = callbackFlow {
        runCatching {
            withContext(Dispatchers.IO) {
                getDrawableResourceBytes(environment, input)
            }
        }.map { bytes ->
            withContext(Dispatchers.Default) {
                bytes.toRawImage()
            }
        }.onSuccess { rawImage ->
            withContext(Dispatchers.Main) {
                send(Resource.Result.Success(data = rawImage))
            }
        }.onFailure {
            withContext(Dispatchers.Main) {
                send(Resource.Result.Failure(it))
            }
        }

        awaitClose()
    }
}
