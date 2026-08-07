package tech.sumato.avn.mp.feature.school_dashboard.presentation.model

import tech.sumato.avn.mp.domain.school.model.SchoolAttendanceModel
import tech.sumato.avn.mp.domain.school.model.SchoolDetailsModel
import tech.sumato.avn.mp.domain.school.model.SchoolExtraFacilityModel
import tech.sumato.avn.mp.domain.school.model.SchoolFacilityModel
import tech.sumato.avn.mp.domain.school.model.SchoolImageModel
import tech.sumato.avn.mp.domain.school.model.SchoolImage360Model
import tech.sumato.avn.mp.domain.school.model.SchoolProjectModel
import tech.sumato.avn.mp.domain.school.model.SchoolRoomConditionModel
import tech.sumato.avn.mp.domain.school.model.SchoolStaffsModel
import tech.sumato.avn.mp.domain.school.model.SchoolStudentsModel

data class SchoolDetailsUiModel(
    val id: String,
    val name: String,
    val category: SchoolCategoryUiModel? = null,
    val udiseCode: String? = null,
    val establishedYear: String? = null,
    val district: DistrictUiModel? = null,
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
)

fun SchoolDetailsModel.toUiModel(): SchoolDetailsUiModel {
    return SchoolDetailsUiModel(
        id = id,
        name = name,
        category = category?.toUiModel(),
        udiseCode = udiseCode,
        establishedYear = establishedYear,
        district = district?.toUiModel(),
        students = students,
        staffs = staffs,
        attendance = attendance,
        classroom = classroom,
        lab = lab,
        totalRooms = totalRooms,
        coreFacilities = coreFacilities,
        extraFacilities = extraFacilities,
        schoolImages = schoolImages,
        schoolImages360 = schoolImages360,
        projects = projects,
    )
}
