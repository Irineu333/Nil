package com.neoutils.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

@Composable
fun App() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    val resource = asyncImageBitmapResource(url = "https://cataas.com/cat")

    when (resource) {
        Resource.Loading -> {
            BasicText(text = "loading...")
        }

        is Resource.Failure -> {
            BasicText(text = "error: ${resource.exception.message}")
        }

        is Resource.Success<ImageBitmap> -> {
            Image(
                bitmap = resource.data,
                contentDescription = null
            )
        }
    }
}

sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>

    data class Success<out T>(
        val data: T
    ) : Resource<T>

    data class Failure(
        val exception: Exception
    ) : Resource<Nothing>
}

val httpClient = HttpClient()

@Composable
fun asyncImageBitmapResource(url: String): Resource<ImageBitmap> {

    val resource = remember { mutableStateOf<Resource<ImageBitmap>>(Resource.Loading) }

    LaunchedEffect(Unit, url) {
        resource.value = try {
            Resource.Success(
                data = httpClient.get(url).bodyAsBytes().decodeToImageBitmap()
            )
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    return resource.value
}
