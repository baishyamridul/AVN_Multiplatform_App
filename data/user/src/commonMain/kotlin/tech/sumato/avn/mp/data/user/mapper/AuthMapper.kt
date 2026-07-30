package tech.sumato.avn.mp.data.user.mapper

import tech.sumato.avn.mp.data.user.remote.LoginResponseDto
import tech.sumato.avn.mp.domain.user.model.AuthResult
import tech.sumato.avn.mp.domain.user.model.User

class AuthMapper {
    fun toDomain(dto: LoginResponseDto): AuthResult {
        return AuthResult(
            tokenType = dto.data!!.tokenType,
            accessToken = dto.data.accessToken,
            user = User(
                id = dto.data.user.id,
                name = dto.data.user.attributes.name,
                email = dto.data.user.attributes.email,
                role = dto.data.user.attributes.role,
                phone = dto.data.user.attributes.phone,
                photo = dto.data.user.attributes.photo,
                designation = dto.data.user.attributes.designation,
            ),
        )
    }
}
