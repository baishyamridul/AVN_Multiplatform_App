package tech.sumato.kmptemplate.data.user.mapper

import tech.sumato.kmptemplate.data.user.remote.LoginResponseDto
import tech.sumato.kmptemplate.domain.user.model.AuthResult
import tech.sumato.kmptemplate.domain.user.model.User

class AuthMapper {
    fun toDomain(dto: LoginResponseDto): AuthResult {
        return AuthResult(
            token = dto.token,
            user = User(
                id = dto.user.id,
                name = dto.user.name,
                email = dto.user.email,
            ),
        )
    }
}
