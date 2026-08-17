package tech.sumato.avn.mp.data.districtDashboard.mapper

import tech.sumato.avn.mp.core.network.model.DateDto
import tech.sumato.avn.mp.data.districtDashboard.dto.DashboardProjectCategoryDto
import tech.sumato.avn.mp.data.districtDashboard.dto.DashboardProjectDto
import tech.sumato.avn.mp.data.districtDashboard.dto.DashboardProjectStatsDto
import tech.sumato.avn.mp.data.districtDashboard.dto.DashboardStatDto
import tech.sumato.avn.mp.data.districtDashboard.dto.DistrictDashboardDataDto
import tech.sumato.avn.mp.data.districtDashboard.dto.DistrictDto
import tech.sumato.avn.mp.data.districtDashboard.dto.OngoingProjectDto
import tech.sumato.avn.mp.data.districtDashboard.dto.SchoolCategoryDto
import tech.sumato.avn.mp.domain.common.model.DateModel
import tech.sumato.avn.mp.domain.common.model.DistrictModel
import tech.sumato.avn.mp.domain.districtDashboard.model.DashboardProjectCategoryData
import tech.sumato.avn.mp.domain.districtDashboard.model.DashboardProjectModel
import tech.sumato.avn.mp.domain.districtDashboard.model.DashboardStatModel
import tech.sumato.avn.mp.domain.districtDashboard.model.DistrictDashboardData
import tech.sumato.avn.mp.domain.districtDashboard.model.DistrictDashboardProjectStatsData
import tech.sumato.avn.mp.domain.districtDashboard.model.OngoingProjectModel
import tech.sumato.avn.mp.domain.districtDashboard.model.SchoolCategoryModel

class DistrictDashboardMapper {

    fun toDomain(dto: DistrictDashboardDataDto): DistrictDashboardData {
        return DistrictDashboardData(
            districts = dto.districts.map { it.toDomain() },
            stats = dto.stats.map { it.toDomain() },
            schoolCategoryList = dto.schoolCategoryList.map { it.toDomain() },
//            ongoingProjects = dto.ongoingProjects.toDomain(),
            ongoingProjects = OngoingProjectModel(emptyList(), 0),
            projectStats = dto.dashboardProjectStats.toDomain(),
        )
    }

    private fun DistrictDto.toDomain() = DistrictModel(id = id, name = name)

    private fun DashboardStatDto.toDomain() = DashboardStatModel(
        label = label,
        value = value,
        description = description,
    )

    private fun SchoolCategoryDto.toDomain() = SchoolCategoryModel(
        key = key,
        schoolCategory = schoolCategory,
        className = className,
        totalSchools = totalSchools,
    )

    private fun OngoingProjectDto.toDomain() = OngoingProjectModel(
        projects = projects.map { it.toDomain() },
        totalProjects = totalProjects,
    )

    private fun DashboardProjectDto.toDomain() = DashboardProjectModel(
        id = id,
        projectName = projectName,
        progressPercent = progressPercent,
        districtName = districtName,
        updatedAt = updatedAt.toDomain(),
    )

    private fun DateDto.toDomain() = DateModel(
        human = human,
        date = date,
        formatted = formatted,
    )

    private fun DashboardProjectStatsDto.toDomain(): DistrictDashboardProjectStatsData {
        return DistrictDashboardProjectStatsData(
            projects = projects.map { it.toDomain() },
            totalProjects = total_projects,
        )
    }

    private fun DashboardProjectCategoryDto.toDomain(): DashboardProjectCategoryData {
        return DashboardProjectCategoryData(
            id = id,
            categoryName = categoryName,
            completedPercent = completedPercent,
            total = total,
            totalCompleted = totalCompleted,
            totalOngoing = totalOngoing,
        )
    }

}
