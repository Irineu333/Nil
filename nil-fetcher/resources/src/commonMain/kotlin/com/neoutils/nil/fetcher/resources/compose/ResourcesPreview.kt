package com.neoutils.nil.fetcher.resources.compose

import androidx.compose.runtime.Composable
import com.neoutils.nil.core.composable.rememberExtras
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.fetcher.resources.extension.resources

@Composable
expect fun ResourcesPreview(
    extras: Extras = rememberExtras {
        fetchers {
            resources()
        }
    },
    content: @Composable () -> Unit
)
