package tech.sumato.avn.mp.data.user.mapper

import tech.sumato.avn.mp.data.user.remote.LoginResponseDto
import tech.sumato.avn.mp.domain.user.model.AuthResult
import tech.sumato.avn.mp.domain.user.model.User

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
