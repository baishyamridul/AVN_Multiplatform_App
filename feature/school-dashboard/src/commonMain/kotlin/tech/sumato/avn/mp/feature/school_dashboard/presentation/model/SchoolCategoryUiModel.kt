package tech.sumato.avn.mp.feature.school_dashboard.presentation.model

import tech.sumato.avn.mp.domain.school.model.SchoolCategoryModel

data class SchoolCategoryUiModel(
    val key: String,
    val name: String,
    val classRange: String
) {
    val label: String
        get() = if (classRange.isNotBlank()) "$name \u2022 $classRange" else name
}

fun SchoolCategoryModel.toUiModel() : SchoolCategoryUiModel {
    return SchoolCategoryUiModel(
        key = key,
        name = name,
        classRange = classRange
    )
}

