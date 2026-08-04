package tech.sumato.avn.mp.data.school.mapper

import tech.sumato.avn.mp.data.school.dto.SchoolCategoryDto
import tech.sumato.avn.mp.data.school.dto.SchoolDistrictDto
import tech.sumato.avn.mp.data.school.dto.SchoolDto
import tech.sumato.avn.mp.domain.common.model.DistrictModel
import tech.sumato.avn.mp.domain.school.model.SchoolCategoryModel
import tech.sumato.avn.mp.domain.school.model.SchoolModel

class SchoolMapper {


    fun toDomain(dto: SchoolDto): SchoolModel {
        return SchoolModel(
            id = dto.id,
            name = dto.name,
            category = dto.category?.toDomain(),
            latitude = dto.latitude,
            longitude = dto.longitude,
            district = dto.district.toDomain()
        )
    }


    fun SchoolCategoryDto.toDomain(): SchoolCategoryModel {
        return SchoolCategoryModel(
            key = key,
            name = name,
            classRange = classRange
        )
    }

    fun SchoolDistrictDto.toDomain(): DistrictModel {
        return DistrictModel(
            id = id,
            name = name
        )
    }

}
