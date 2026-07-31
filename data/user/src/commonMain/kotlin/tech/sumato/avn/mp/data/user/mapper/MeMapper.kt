package tech.sumato.avn.mp.data.user.mapper

import tech.sumato.avn.mp.core.network.model.DateDto
import tech.sumato.avn.mp.data.user.dto.DistrictDto
import tech.sumato.avn.mp.data.user.dto.MeDataDto
import tech.sumato.avn.mp.domain.common.model.DateModel
import tech.sumato.avn.mp.domain.common.model.DistrictModel
import tech.sumato.avn.mp.domain.user.model.UserDetailsModel

class MeMapper {

    fun toDomain(dto: MeDataDto): UserDetailsModel {
        return UserDetailsModel(
            id = dto.id,
            name = dto.name,
            email = dto.email,
            role = dto.role,
            phone = dto.phone,
            photo = dto.photo,
            designation = dto.designation,
            created = dto.created?.let { toDomain(it) },
            districts = dto.district.orEmpty().map { toDomain(it) },
        )
    }

    fun toDomain(dto: DateDto) = DateModel(
        human = dto.human,
        date = dto.date,
        formatted = dto.formatted,
    )

    fun toDomain(dto: DistrictDto) = DistrictModel(
        id = dto.id,
        name = dto.name,
    )
}
