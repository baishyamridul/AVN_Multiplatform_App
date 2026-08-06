package tech.sumato.avn.mp.feature.district_dashboard.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDate

data class ExaminationEventUiModel(
    val name: String,
    val subname: String,
    val eligibility: String,
    val date: String, //yyyy-MM-dd
)

fun getExaminationEventDateColor(): Color {
    return listOf(
        Color(0xffFFBA00),
        Color(0xff00D3F3),
        Color(0xffC27BFF),
        Color(0xff00D492),
    ).random()
}

fun ExaminationEventUiModel.getParsedDate() : LocalDate {
    return LocalDate.parse(date)
}

