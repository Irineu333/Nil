package com.neoutils.nil.fetcher.resources.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.fetcher.resources.model.RequestResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.rememberResourceEnvironment

@Composable
fun Request.Companion.resource(
    res: DrawableResource,
    environment: ResourceEnvironment = rememberResourceEnvironment()
) = remember(res, environment) {
    RequestResource(res, environment)
}
