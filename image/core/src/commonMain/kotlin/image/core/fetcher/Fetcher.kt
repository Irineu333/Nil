package image.core.fetcher

import androidx.compose.runtime.Composable
import image.core.util.Input
import image.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

abstract class Fetcher<out T : Input>(val type: KClass<@UnsafeVariance T>) {

    @Composable
    open fun Prepare() = Unit

    abstract suspend fun get(input: @UnsafeVariance T): Resource.Result<ByteArray>

    abstract fun fetch(input: @UnsafeVariance T): Flow<Resource<ByteArray>>
}