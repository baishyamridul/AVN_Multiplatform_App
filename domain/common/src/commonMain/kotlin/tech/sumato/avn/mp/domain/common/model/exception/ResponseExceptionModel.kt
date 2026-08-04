package tech.sumato.avn.mp.domain.common.model.exception

data class ResponseExceptionModel(
    override val message: String,
    val fieldErrors: Map<String, List<String>>? = null,
) : Exception(message)
