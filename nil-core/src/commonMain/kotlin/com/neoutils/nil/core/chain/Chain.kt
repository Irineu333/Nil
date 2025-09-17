package com.neoutils.nil.core.chain

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.flow.Flow

data class Chain(
    val request: Request,
    val data: Resource<ByteArray>? = null,
    val painter: Resource<Painter>? = null
)
