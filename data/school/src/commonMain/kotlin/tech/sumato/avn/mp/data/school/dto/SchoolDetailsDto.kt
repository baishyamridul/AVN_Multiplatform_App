package tech.sumato.avn.mp.data.school.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SchoolDetailsDto(
    val id: String,
    val name: String,
    val category: SchoolCategoryDto? = null,
    @SerialName("udise_code")
    val udiseCode: String? = null,
    @SerialName("established_year")
    val establishedYear: String? = null,
    val district: SchoolDistrictDto? = null,
    val students: SchoolStudentsDto? = null,
    val staffs: SchoolStaffsDto? = null,
    val attendance: SchoolAttendanceDto? = null,
    val classroom: SchoolRoomConditionDto? = null,
    val lab: SchoolRoomConditionDto? = null,
    @SerialName("total_rooms")
    val totalRooms: Int? = null,
    @SerialName("core_facilities")
    val coreFacilities: List<SchoolFacilityDto> = emptyList(),
    @SerialName("extra_facilities")
    val extraFacilities: List<SchoolExtraFacilityDto> = emptyList(),
    @SerialName("school_images")
    val schoolImages: List<SchoolImageDto> = emptyList(),
    @SerialName("school_images_360")
    val schoolImages360: List<SchoolImage360Dto> = emptyList(),
    val projects: List<SchoolProjectDto> = emptyList(),
)

@Serializable
data class SchoolStudentsDto(
    val boys: Int,
    val girls: Int,
    val total: Int,
)

@Serializable
data class SchoolStaffsDto(
    val total: Int,
)

@Serializable
data class SchoolAttendanceDto(
    val student: SchoolAttendanceStatusDto? = null,
    val staff: SchoolAttendanceStatusDto? = null,
)

@Serializable
data class SchoolAttendanceStatusDto(
    val present: Int,
    val percent: Double,
)

@Serializable
data class SchoolRoomConditionDto(
    @SerialName("very_good")
    val veryGood: SchoolRoomCountDto? = null,
    val good: SchoolRoomCountDto? = null,
    val poor: SchoolRoomCountDto? = null,
)

@Serializable
data class SchoolRoomCountDto(
    val count: Int,
    val percent: Double,
)

@Serializable
data class SchoolFacilityDto(
    val key: String,
    val label: String,
    val value: String? = null,
)

@Serializable
data class SchoolExtraFacilityDto(
    val key: String,
    val label: String,
)

@Serializable
data class SchoolImageDto(
    val thumbnail: String,
    val large: String,
    val caption: String? = null,
)

@Serializable
data class SchoolImage360Dto(
    val thumbnail: String,
    val link: String,
    val caption: String? = null,
)

@Serializable
data class SchoolProjectDto(
    val id: String,
    val name: String,
    val category: String? = null,
    val status: String? = null,
    val percent: Int = 0,
    @SerialName("allocated_amount")
    val allocatedAmount: Long? = null,
)