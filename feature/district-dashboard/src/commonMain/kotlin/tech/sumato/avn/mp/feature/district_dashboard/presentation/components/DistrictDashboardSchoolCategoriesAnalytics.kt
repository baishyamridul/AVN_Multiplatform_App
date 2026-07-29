package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.designsystem.components.school.AppSchoolCategoryCard
import tech.sumato.avn.mp.domain.districtDashboard.model.SchoolCategoryModel
import tech.sumato.avn.mp.feature.district_dashboard.presentation.model.SchoolCategoryUiModel

@Composable
fun DistrictDashboardSchoolCategoriesAnalytics(modifier: Modifier, categories: List<SchoolCategoryModel>) {

    AppCardBordered(
    modifier = modifier,
    ) {
        Text(
            "\uD83C\uDFEB School Categories Analytics",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            listOf(
                SchoolCategoryUiModel("1", "Primary School", "I - V", "1,420"),
                SchoolCategoryUiModel("2", "Middle Schools", "VI - VIII", "984"),
                SchoolCategoryUiModel("3", "High Schools", "IX - X", "512"),
                SchoolCategoryUiModel(
                    "4",
                    "Higher Secondary Schools",
                    "XI - XII",
                    "332"
                ),
            ).forEach { item ->
                AppSchoolCategoryCard(
                    modifier = Modifier.fillMaxWidth(),
                    name = item.label,
                    schoolCount = item.schoolCount,
                    classRange = item.classRange,
                )
            }


            /*items(
                listOf(
                    SchoolCategoryUiModel("1", "Primary School", "I - V", "1,420"),
                    SchoolCategoryUiModel("2", "Middle Schools", "VI - VIII", "984"),
                    SchoolCategoryUiModel("3", "High Schools", "IX - X", "512"),
                    SchoolCategoryUiModel(
                        "4",
                        "Higher Secondary Schools",
                        "XI - XII",
                        "332"
                    ),
                ),
            ) { item ->
                AppSchoolCategoryCard(
                    modifier = Modifier.fillMaxWidth(),
                    name = item.label,
                    schoolCount = item.schoolCount,
                    classRange = item.classRange,
                )
            }*/
        }
    }

}