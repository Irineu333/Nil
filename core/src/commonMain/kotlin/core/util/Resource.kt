package core.util

sealed interface Resource<out T> {

    data class Loading(
        val progress: Float? = null
    ) : Resource<Nothing>

    sealed interface Result<out T> : Resource<T> {

        data class Success<out T>(
            val data: T
        ) : Result<T>

        data class Failure(
            val throwable: Throwable
        ) : Result<Nothing>
    }
}