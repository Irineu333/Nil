package image.core.fetcher

import image.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface Fetcher<T> {
    suspend fun get(input: T): Resource.Result<ByteArray>
    fun fetch(input: T): Flow<Resource<ByteArray>>
}