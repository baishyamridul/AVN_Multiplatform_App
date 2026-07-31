package tech.sumato.avn.mp.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SuccessResponseWrapper<T>(
    val status: Int,
    val message: String,
    val data: T? = null,
)

@Serializable
data class ErrorResponseWrapper(
    val status: Int,
    val message: String,
    val errors: Map<String, List<String>>? = null,
)
