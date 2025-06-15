package core.fetcher

import core.util.RawImage
import core.util.Resource
import kotlinx.coroutines.flow.Flow

interface Fetcher<T> {
    suspend fun get(input: T) : Resource.Result<RawImage>
    fun fetch(input: T):  Flow<Resource<RawImage>>
}