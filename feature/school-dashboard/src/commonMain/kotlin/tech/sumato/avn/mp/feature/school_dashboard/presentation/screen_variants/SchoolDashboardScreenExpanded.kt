package tech.sumato.avn.mp.feature.school_dashboard.presentation.screen_variants

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import tech.sumato.avn.mp.component.map.MapView
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.designsystem.components.AppTextField
import tech.sumato.avn.mp.designsystem.components.app.AppChip
import tech.sumato.avn.mp.designsystem.components.app.fields.AppDropDownBasic
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.SchoolDashboardHeader
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.SchoolsMapLayers
import tech.sumato.avn.mp.feature.school_dashboard.presentation.event.SchoolDashboardEvent
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.toUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolDashboardState
import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolSortOption


@Composable
fun SchoolDashboardScreenExpanded(
    state: SchoolDashboardState,
    onEvent: (SchoolDashboardEvent) -> Unit
) {


    var loadMap by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)

        loadMap = true

    }


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        SchoolDashboardHeader(
            modifier = Modifier.fillMaxWidth(),
            districts = state.schoolsState.schools.map { it.district.name }.distinct().sorted(),
            selectedDistrict = state.schoolsState.selectedDistrict,
            onDistrictSelected = { district ->
                onEvent(SchoolDashboardEvent.SelectDistrict(district))
            },
            onBack = {
                onEvent(SchoolDashboardEvent.Back)
            }
        )


        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            AppCardBordered(
                modifier = Modifier.weight(3f).fillMaxSize(),
                paddingLess = true,
            ) {

                if (loadMap)
                    MapView(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        styleUrl = "",
                    ) {
//                        SchoolsMapLayers(
//                            schools = state.schoolsState.schools.map { it.toUiModel() },
//                            selectedSchoolId = state.schoolsState.selectedSchoolId
//                        )
                    }
                else
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                    )
            }

            AppCardBordered(
                modifier = Modifier.weight(1.8f).fillMaxSize(),
                paddingLess = false,
            ) {
                Text(
                    "\uD83C\uDFEB Schools",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(16.dp))

                SchoolListContent(
                    schools = state.schoolsState.schools.map { it.toUiModel() },
                    selectedSchoolId = state.schoolsState.selectedSchoolId,
                    searchQuery = state.schoolsState.searchQuery,
                    selectedDistrict = state.schoolsState.selectedDistrict,
                    sortOption = state.schoolsState.sortOption,
                    onSearchQueryChange = { query ->
                        onEvent(SchoolDashboardEvent.UpdateSearchQuery(query))
                    },
                    onSortOptionChange = { option ->
                        onEvent(SchoolDashboardEvent.SelectSortOption(option))
                    },
                    onSelectSchool = { schoolId ->
                        val isSelected = schoolId == state.schoolsState.selectedSchoolId
                        if (isSelected) {
                            onEvent(SchoolDashboardEvent.ClearSchoolSelection)
                        } else {
                            onEvent(SchoolDashboardEvent.SelectSchool(schoolId))
                        }
                    }
                )


            }

        }


    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SchoolListContent(
    schools: List<SchoolUiModel>,
    selectedSchoolId: String?,
    searchQuery: String,
    selectedDistrict: String?,
    sortOption: SchoolSortOption,
    onSearchQueryChange: (String) -> Unit,
    onSortOptionChange: (SchoolSortOption) -> Unit,
    onSelectSchool: (String) -> Unit,
) {
    val filteredSchools = schools
        .filter { school ->
            val matchesQuery = searchQuery.isBlank() ||
                    school.name.contains(searchQuery, ignoreCase = true)
            val matchesDistrict = selectedDistrict == null ||
                    school.districtModel.name == selectedDistrict
            matchesQuery && matchesDistrict
        }

    Column(modifier = Modifier.fillMaxSize()) {

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppChip(modifier = Modifier.wrapContentWidth()) {
                Row(
                    modifier = Modifier.wrapContentWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.FilterList, "", modifier = Modifier.size(16.dp))
                    Text(
                        "Sort",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = LocalContentColor.current.copy(alpha = 0.85f),
                    )
                }
            }

            AppChip(modifier = Modifier.wrapContentWidth()) {
                Row(
                    modifier = Modifier.wrapContentWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Category, "", modifier = Modifier.size(16.dp))
                    Text(
                        "Categories",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = LocalContentColor.current.copy(alpha = 0.85f),
                    )
                }
            }

            AppChip(modifier = Modifier.wrapContentWidth()) {
                Row(
                    modifier = Modifier.wrapContentWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Place, "", modifier = Modifier.size(16.dp))
                    Text(
                        "District",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = LocalContentColor.current.copy(alpha = 0.85f),
                    )
                }
            }


//            AppChip(modifier = Modifier.wrapContentWidth()) {
//                Row(
//                    modifier = Modifier.wrapContentWidth()
//                        .padding(horizontal = 8.dp, vertical = 4.dp),
//                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Icon(Icons.Outlined.Search, "", modifier = Modifier.size(16.dp))
//                    Text(
//                        "Search School",
//                        style = MaterialTheme.typography.labelMedium,
//                        fontWeight = FontWeight.Medium,
//                        color = LocalContentColor.current.copy(alpha = 0.85f),
//                    )
//                }
//            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = "Search",
            placeholder = "Search by school name",
            modifier = Modifier.fillMaxWidth(),
            trailing = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchQueryChange("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredSchools.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "No schools found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (sortOption == SchoolSortOption.DistrictName) {
                    val groupedSchools = filteredSchools
                        .sortedBy { it.name }
                        .groupBy { it.districtModel.name }
                        .toList()
                        .sortedBy { it.first }

                    groupedSchools.forEach { (district, districtSchools) ->
                        stickyHeader(key = district) {
                            Text(
                                text = district,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(vertical = 8.dp)
                            )
                        }

                        items(districtSchools, key = { it.id }) { school ->
                            SchoolListItem(
                                school = school,
                                isSelected = school.id == selectedSchoolId,
                                onClick = { onSelectSchool(school.id) }
                            )
                        }
                    }
                } else {
                    items(filteredSchools.sortedBy { it.name }, key = { it.id }) { school ->
                        SchoolListItem(
                            school = school,
                            isSelected = school.id == selectedSchoolId,
                            onClick = { onSelectSchool(school.id) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SchoolListItem(
    school: SchoolUiModel,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    AppCardBordered(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        onClick = onClick,
        containerColor = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    buildString {
                        append(school.name)
                        append(" \u2022 ").append(school.districtModel.name)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    buildString {
                        school.category?.name?.let { append(it) }
                        school.category?.classRange?.let { append(" \u2022 ").append(it) }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (school.hasLocation()) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Has location",
                    tint = Color(0xff65a30d)
//                    tint = Color(0xffdc2626)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.LocationOff,
                    contentDescription = "No location",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                )
            }
        }
    }
}
