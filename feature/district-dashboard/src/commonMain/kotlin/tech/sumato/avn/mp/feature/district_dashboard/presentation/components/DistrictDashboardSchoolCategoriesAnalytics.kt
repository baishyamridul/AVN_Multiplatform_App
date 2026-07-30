package tech.sumato.avn.mp.feature.district_dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun DistrictDashboardSchoolCategoriesAnalytics(
    modifier: Modifier,
    categories: List<SchoolCategoryUiModel> = emptyList()
) {

    AppCardBordered(
        modifier = modifier,
    ) {
        Text(
            "\uD83C\uDFEB School Categories Analytics",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            items(categories) { category ->
                AppSchoolCategoryCard(
                    modifier = Modifier.fillMaxWidth(),
                    name = category.label,
                    schoolCount = category.schoolCount.toString(),
                    classRange = category.classRange,
                )
            }


        }
    }

}