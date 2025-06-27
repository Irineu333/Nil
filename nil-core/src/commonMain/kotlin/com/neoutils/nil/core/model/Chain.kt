package com.neoutils.nil.core.model

import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Request
import com.neoutils.nil.core.util.Resource

data class Chain(
    val request: Request,
    val data: Resource<ByteArray> = Resource.Loading(),
    val painter: PainterResource = PainterResource.Loading(),
)