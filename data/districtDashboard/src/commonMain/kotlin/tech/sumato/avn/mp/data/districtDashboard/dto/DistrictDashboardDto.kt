package tech.sumato.avn.mp.data.districtDashboard.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sumato.avn.mp.core.network.model.DateDto

@Serializable
data class DistrictDashboardDataDto(
    val districts: List<DistrictDto>,
    val stats: List<DashboardStatDto>,
    @SerialName("school_category_list")
    val schoolCategoryList: List<SchoolCategoryDto>,
    @SerialName("ongoing_projects")
    val ongoingProjects: OngoingProjectDto,
)

@Serializable
data class DistrictDto(
    val id: Int,
    val name: String,
)

@Serializable
data class DashboardStatDto(
    val label: String,
    val value: String,
    val description: String? = null,
)

@Serializable
data class SchoolCategoryDto(
    @SerialName("school_category")
    val schoolCategory: String,
    @SerialName("class")
    val className: String,
    @SerialName("total_schools")
    val totalSchools: Int,
)

@Serializable
data class OngoingProjectDto(
    val projects: List<DashboardProjectDto>,
    @SerialName("total_projects")
    val totalProjects: Int,
)

@Serializable
data class DashboardProjectDto(
    val id: String,
    @SerialName("project_name")
    val projectName: String,
    @SerialName("progress_percent")
    val progressPercent: Int,
    @SerialName("district_name")
    val districtName: String,
    @SerialName("updated_at")
    val updatedAt: DateDto,
)
