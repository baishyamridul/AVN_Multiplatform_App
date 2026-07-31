package tech.sumato.avn.mp.data.user.mapper

import tech.sumato.avn.mp.core.network.model.SuccessResponseWrapper
import tech.sumato.avn.mp.data.user.remote.LoginDataDto
import tech.sumato.avn.mp.domain.user.model.AuthResult
import tech.sumato.avn.mp.domain.user.model.User

class AuthMapper {
    fun toDomain(dto: SuccessResponseWrapper<LoginDataDto>): AuthResult {
        val data = dto.data ?: throw IllegalStateException("Login data is missing")
        return AuthResult(
            tokenType = data.tokenType,
            accessToken = data.accessToken,
            user = User(
                id = data.user.id,
                name = data.user.attributes.name,
                email = data.user.attributes.email,
                role = data.user.attributes.role,
                phone = data.user.attributes.phone,
                photo = data.user.attributes.photo,
                designation = data.user.attributes.designation,
            ),
        )
    }
}
