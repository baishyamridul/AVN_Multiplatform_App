package tech.sumato.avn.mp.data.school.mapper

import tech.sumato.avn.mp.core.network.BaseUrls
import tech.sumato.avn.mp.data.school.dto.SchoolCategoryDto
import tech.sumato.avn.mp.data.school.dto.SchoolDetailsDto
import tech.sumato.avn.mp.data.school.dto.SchoolDistrictDto
import tech.sumato.avn.mp.data.school.dto.SchoolDto
import tech.sumato.avn.mp.data.school.dto.SchoolImageDto
import tech.sumato.avn.mp.data.school.dto.SchoolImage360Dto
import tech.sumato.avn.mp.data.school.dto.SchoolRoomConditionDto
import tech.sumato.avn.mp.domain.common.model.DistrictModel
import tech.sumato.avn.mp.domain.school.model.SchoolAttendanceModel
import tech.sumato.avn.mp.domain.school.model.SchoolAttendanceStatusModel
import tech.sumato.avn.mp.domain.school.model.SchoolCategoryModel
import tech.sumato.avn.mp.domain.school.model.SchoolDetailsModel
import tech.sumato.avn.mp.domain.school.model.SchoolExtraFacilityModel
import tech.sumato.avn.mp.domain.school.model.SchoolFacilityModel
import tech.sumato.avn.mp.domain.school.model.SchoolImageModel
import tech.sumato.avn.mp.domain.school.model.SchoolImage360Model
import tech.sumato.avn.mp.domain.school.model.SchoolModel
import tech.sumato.avn.mp.domain.school.model.SchoolProjectModel
import tech.sumato.avn.mp.domain.school.model.SchoolRoomConditionModel
import tech.sumato.avn.mp.domain.school.model.SchoolRoomCountModel
import tech.sumato.avn.mp.domain.school.model.SchoolStaffsModel
import tech.sumato.avn.mp.domain.school.model.SchoolStudentsModel

class SchoolMapper(private val baseUrls: BaseUrls) {


    fun toDomain(dto: SchoolDto): SchoolModel {
        return SchoolModel(
            id = dto.id,
            name = dto.name,
            udise = dto.udise,
            category = dto.category?.toDomain(),
            latitude = dto.latitude,
            longitude = dto.longitude,
            district = dto.district.toDomain(),
            qrUrl = "${baseUrls.baseUrl}school/${dto.id}/qr-scan",
            goldenJubilee = dto.goldenJubilee,
            pmShri = dto.pmShri
        )
    }


    fun SchoolCategoryDto.toDomain(): SchoolCategoryModel {
        return SchoolCategoryModel(
            key = key,
            name = name,
            classRange = classRange
        )
    }

    fun SchoolRoomConditionDto.toDomain(): SchoolRoomConditionModel {
        return SchoolRoomConditionModel(
            veryGood = veryGood?.let {
                SchoolRoomCountModel(count = it.count, percent = it.percent)
            },
            good = good?.let {
                SchoolRoomCountModel(count = it.count, percent = it.percent)
            },
            poor = poor?.let {
                SchoolRoomCountModel(count = it.count, percent = it.percent)
            },
        )
    }

    fun SchoolImageDto.toDomain(): SchoolImageModel {
        return SchoolImageModel(
            thumbnail = thumbnail,
            large = large,
            caption = caption,
        )
    }

    fun SchoolImage360Dto.toDomain(): SchoolImage360Model {
        return SchoolImage360Model(
            thumbnail = thumbnail,
            link = link,
            caption = caption,
        )
    }

    fun SchoolDistrictDto.toDomain(): DistrictModel {
        return DistrictModel(
            id = id,
            name = name
        )
    }

    fun toDomain(dto: SchoolDetailsDto): SchoolDetailsModel {
        return SchoolDetailsModel(
            id = dto.id,
            name = dto.name,
            category = dto.category?.toDomain(),
            udiseCode = dto.udiseCode,
            establishedYear = dto.establishedYear,
            district = dto.district?.toDomain(),
            students = dto.students?.let {
                SchoolStudentsModel(boys = it.boys, girls = it.girls, total = it.total)
            },
            staffs = dto.staffs?.let { SchoolStaffsModel(total = it.total) },
            attendance = dto.attendance?.let {
                SchoolAttendanceModel(
                    student = it.student?.let { a ->
                        SchoolAttendanceStatusModel(present = a.present, percent = a.percent)
                    },
                    staff = it.staff?.let { a ->
                        SchoolAttendanceStatusModel(present = a.present, percent = a.percent)
                    },
                )
            },
            classroom = dto.classroom?.toDomain(),
            lab = dto.lab?.toDomain(),
            totalRooms = dto.totalRooms,
            coreFacilities = dto.coreFacilities.map { f ->
                SchoolFacilityModel(key = f.key, label = f.label, value = f.value)
            },
            extraFacilities = dto.extraFacilities.map { f ->
                SchoolExtraFacilityModel(key = f.key, label = f.label)
            },
            schoolImages = dto.schoolImages.map { it.toDomain() },
            schoolImages360 = dto.schoolImages360.map { it.toDomain() },
            projects = dto.projects.map { p ->
                SchoolProjectModel(
                    id = p.id,
                    name = p.name,
                    category = p.category,
                    status = p.status,
                    percent = p.percent,
                    allocatedAmount = p.allocatedAmount,
                )
            },
            qrUrl = "${baseUrls.baseUrl}/school/${dto.id}/qr-scan"
        )
    }

}
