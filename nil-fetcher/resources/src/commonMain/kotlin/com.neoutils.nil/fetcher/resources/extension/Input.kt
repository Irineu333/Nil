package com.neoutils.nil.fetcher.resources.extension

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.fetcher.resources.model.InputResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.rememberResourceEnvironment

@Composable
fun Input.Companion.resource(
    res: DrawableResource,
    environment: ResourceEnvironment = rememberResourceEnvironment()
) = InputResource(res, environment)
