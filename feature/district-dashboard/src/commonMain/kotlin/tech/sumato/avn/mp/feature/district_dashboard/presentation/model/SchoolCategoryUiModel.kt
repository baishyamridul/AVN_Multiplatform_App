package tech.sumato.avn.mp.feature.district_dashboard.presentation.model

import kotlinx.serialization.Serializable


@Serializable
data class SchoolCategoryUiModel(
    val id: String,
    val label: String,
    val classRange: String,
    val schoolCount: String,
)