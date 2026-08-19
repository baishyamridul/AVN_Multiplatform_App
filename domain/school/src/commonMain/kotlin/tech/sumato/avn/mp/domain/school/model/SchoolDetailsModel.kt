package tech.sumato.avn.mp.domain.school.model

import tech.sumato.avn.mp.domain.common.model.DistrictModel
import kotlin.math.absoluteValue
import kotlin.math.round

data class SchoolDetailsModel(
    val id: String,
    val name: String,
    val category: SchoolCategoryModel? = null,
    val udiseCode: String? = null,
    val establishedYear: String? = null,
    val district: DistrictModel? = null,
    val students: SchoolStudentsModel? = null,
    val staffs: SchoolStaffsModel? = null,
    val attendance: SchoolAttendanceModel? = null,
    val classroom: SchoolRoomConditionModel? = null,
    val lab: SchoolRoomConditionModel? = null,
    val totalRooms: Int? = null,
    val coreFacilities: List<SchoolFacilityModel> = emptyList(),
    val extraFacilities: List<SchoolExtraFacilityModel> = emptyList(),
    val schoolImages: List<SchoolImageModel> = emptyList(),
    val schoolImages360: List<SchoolImage360Model> = emptyList(),
    val projects: List<SchoolProjectModel> = emptyList(),
    val qrUrl: String,
)

data class SchoolStudentsModel(
    val boys: Int,
    val girls: Int,
    val total: Int,
) {
    fun getBoysPercent(): Double {
        val p = (boys.toDouble() / total.toDouble()) * 100
        return round(p.absoluteValue * 100) / 100
    }

    fun getGirlsPercent(): Double {
        val p = (girls.toDouble() / total.toDouble()) * 100
        return round(p.absoluteValue * 100) / 100
    }
}

data class SchoolStaffsModel(
    val total: Int,
)

data class SchoolAttendanceModel(
    val student: SchoolAttendanceStatusModel? = null,
    val staff: SchoolAttendanceStatusModel? = null,
)

data class SchoolAttendanceStatusModel(
    val present: Int,
    val percent: Double,
)

data class SchoolRoomConditionModel(
    val veryGood: SchoolRoomCountModel? = null,
    val good: SchoolRoomCountModel? = null,
    val poor: SchoolRoomCountModel? = null,
)

data class SchoolRoomCountModel(
    val count: Int,
    val percent: Double,
)

data class SchoolFacilityModel(
    val key: String,
    val label: String,
    val value: String? = null,
)

data class SchoolExtraFacilityModel(
    val key: String,
    val label: String,
)

data class SchoolImageModel(
    val thumbnail: String,
    val large: String,
    val caption: String? = null,
)

data class SchoolImage360Model(
    val thumbnail: String,
    val link: String,
    val caption: String? = null,
)

data class SchoolProjectModel(
    val id: String,
    val name: String,
    val category: String? = null,
    val status: String? = null,
    val percent: Int = 0,
    val allocatedAmount: Long? = null,
)
