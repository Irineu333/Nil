package core.fetcher

import core.extension.toRawImage
import core.util.RawImage
import core.util.Resource
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

class NetworkFetcher(
    private val client: HttpClient = HttpClient()
) : Fetcher<String> {
    override suspend fun get(input: String): Resource.Result<RawImage> {
        return runCatching {
            client.get(input)
        }.map {
            withContext(Dispatchers.Default) {
                it.bodyAsBytes().toRawImage()
            }
        }.map { rawImage ->
            Resource.Result.Success(data = rawImage)
        }.getOrElse {
            Resource.Result.Failure(it)
        }
    }

    override fun fetch(input: String) = channelFlow {
        runCatching {
            client.get(input) {
                onProgress { progress ->
                    withContext(Dispatchers.Main) {
                        send(Resource.Loading(progress))
                    }
                }
            }
        }.map {
            withContext(Dispatchers.Default) {
                it.bodyAsBytes().toRawImage()
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

private fun HttpRequestBuilder.onProgress(
    update: suspend (progress: Float?) -> Unit
) = onDownload { bytesSentTotal, contentLength ->
    update(
        contentLength?.let {
            bytesSentTotal.toFloat()
                .div(contentLength.toFloat())
                .coerceIn(minimumValue = 0f, maximumValue = 1f)
                .takeUnless { it.isNaN() }
        }
    )
}
