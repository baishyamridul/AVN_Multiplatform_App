package tech.sumato.avn.mp.feature.school_dashboard.presentation.screen_variants

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Place
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import tech.sumato.avn.mp.component.map.GeoJsonLoader
import tech.sumato.avn.mp.component.map.MapHolder
import tech.sumato.avn.mp.component.map.MapHolderBoundingBox
import tech.sumato.avn.mp.component.map.MapHolderCameraState
import tech.sumato.avn.mp.component.map.MapHolderPosition
import tech.sumato.avn.mp.component.map.MapHolderState
import tech.sumato.avn.mp.component.map.MapView
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.designsystem.components.AppTextField
import tech.sumato.avn.mp.designsystem.components.app.AppChip
import tech.sumato.avn.mp.designsystem.components.app.fields.AppDropDownBasic
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.MapPanelShimmer
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.SchoolDashboardHeader
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.SchoolDetails
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.SchoolDetailsShimmer
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.SchoolListShimmer
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.SchoolsMapLayers
import tech.sumato.avn.mp.feature.school_dashboard.presentation.event.SchoolDashboardEvent
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.DistrictUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolCategoryUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolDetailsUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.toUiModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolDashboardState
import tech.sumato.avn.mp.feature.school_dashboard.presentation.state.SchoolSortOption
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun SchoolDashboardScreenExpanded(
    state: SchoolDashboardState, onEvent: (SchoolDashboardEvent) -> Unit
) {


//    val arunachalBoundary by produceState<String?>(initialValue = null) {
//        value = GeoJsonLoader.loadArunachalBoundary()
//    }
//
//    val arunachalBoundarySource = rememberGeoJsonSource(
//        data = GeoJsonData.JsonString(arunachalBoundary ?: "not read")
//    )


    var loadMap by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000.milliseconds)

        loadMap = true

    }

    val allSchools = remember(state.schoolsState.schools) {
        state.schoolsState.schools.map { it.toUiModel() }
    }

    val allCategories = remember(allSchools) {
        allSchools.mapNotNull { it.category }.distinctBy { it.key }.sortedBy { it.label }
    }

    val filteredSchools = remember(
        allSchools,
        state.schoolsState.searchQuery,
        state.schoolsState.selectedDistrictId,
        state.schoolsState.selectedCategory,
    ) {
        filterSchools(
            schools = allSchools,
            searchQuery = state.schoolsState.searchQuery,
            selectedDistrictId = state.schoolsState.selectedDistrictId,
            selectedCategory = state.schoolsState.selectedCategory,
        )
    }

    val selectedSchool = remember(allSchools, state.schoolsState.selectedSchoolId) {
        allSchools.firstOrNull { it.id == state.schoolsState.selectedSchoolId }
    }

    var mapResetVersion by remember { mutableIntStateOf(0) }


    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        SchoolDashboardHeader(
            modifier = Modifier.fillMaxWidth(),
            districts = state.schoolsState.districts.map { it.toUiModel() },
            selectedDistrictId = state.schoolsState.selectedDistrictId,
            onDistrictSelected = { districtId ->
                onEvent(SchoolDashboardEvent.SelectDistrict(districtId))
            },
            onBack = {
                onEvent(SchoolDashboardEvent.Back)
            })


        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            AppCardBordered(
                modifier = Modifier.weight(3f).fillMaxSize(),
                paddingLess = true,
            ) {

                if (state.schoolsState.isLoading) {
                    MapPanelShimmer()
                } else if (loadMap) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MapHolder(
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                            mapHolderState = MapHolderState(
                                cameraState = MapHolderCameraState(
                                    boundingBox = MapHolderBoundingBox(),
                                    focusPosition = selectedSchool?.takeIf { it.hasLocation() }
                                        ?.let { MapHolderPosition(it.latitude!!, it.longitude!!) },
                                    resetVersion = mapResetVersion,
                                )
                            ),
                            disable = false
                        ) {

                            SchoolsMapLayers(
                                schools = filteredSchools,
                                categories = allCategories,
                                selectedSchoolId = state.schoolsState.selectedSchoolId
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(40.dp)
                                .shadow(elevation = 4.dp, shape = CircleShape),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        ) {
                            IconButton(
                                onClick = {
//                                    onEvent(SchoolDashboardEvent.ClearSchoolSelection)
                                    mapResetVersion++
                                },
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Icon(Icons.Default.LockReset, "Reset Map")
                            }
                        }

                    }

                } else Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
                )
            }

            AppCardBordered(
                modifier = Modifier.weight(1.8f).fillMaxSize(),
                paddingLess = false,
            ) {

                val selectedSchoolId = state.schoolsState.selectedSchoolId

                if (selectedSchoolId != null) {

                    SchoolDetailsPanel(
                        isSchoolDetailsLoading = state.schoolsState.isSchoolDetailsLoading,
                        schoolDetails = state.schoolsState.schoolDetails,
                        onBack = {
                            onEvent(SchoolDashboardEvent.ClearSchoolSelection)
                            onEvent(SchoolDashboardEvent.ClearSchoolDetails)
                        })

                } else if (state.schoolsState.isLoading) {

                    SchoolListShimmer()

                } else {

                    SchoolListContent(
                        schools = filteredSchools,
                        allCategories = allCategories,
                        districts = state.schoolsState.districts.map { it.toUiModel() },
                        selectedSchoolId = state.schoolsState.selectedSchoolId,
                        searchQuery = state.schoolsState.searchQuery,
                        selectedDistrictId = state.schoolsState.selectedDistrictId,
                        selectedCategory = state.schoolsState.selectedCategory,
                        sortOption = state.schoolsState.sortOption,
                        isLoading = state.schoolsState.isLoading,
                        onSearchQueryChange = { query ->
                            onEvent(SchoolDashboardEvent.UpdateSearchQuery(query))
                        },
                        onDistrictSelected = { districtId ->
                            onEvent(SchoolDashboardEvent.SelectDistrict(districtId))
                        },
                        onCategorySelected = { category ->
                            onEvent(SchoolDashboardEvent.SelectCategory(category))
                        },
                        onSortOptionChange = { option ->
                            onEvent(SchoolDashboardEvent.SelectSortOption(option))
                        },
                        onSelectSchool = { schoolId ->
                            onEvent(SchoolDashboardEvent.SelectSchool(schoolId))
                            onEvent(SchoolDashboardEvent.LoadSchoolDetails(schoolId))
                        })

                }

            }

        }


    }

}


@Composable
private fun SchoolDetailsPanel(
    isSchoolDetailsLoading: Boolean,
    schoolDetails: SchoolDetailsUiModel?,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {

        IconButton(
            onClick = onBack,
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to schools",
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            isSchoolDetailsLoading -> {
                SchoolDetailsShimmer()
            }

            schoolDetails != null -> {
                SchoolDetails(details = schoolDetails)
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Unable to load school details",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SchoolListContent(
    schools: List<SchoolUiModel>,
    allCategories: List<SchoolCategoryUiModel>,
    districts: List<DistrictUiModel>,
    selectedSchoolId: String?,
    searchQuery: String,
    selectedDistrictId: Int?,
    selectedCategory: String?,
    sortOption: SchoolSortOption,
    isLoading: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onDistrictSelected: (Int?) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onSortOptionChange: (SchoolSortOption) -> Unit,
    onSelectSchool: (String) -> Unit,
) {
//    val filteredSchools = schools
//        .filter { school ->
//            val matchesQuery = searchQuery.isBlank() ||
//                    school.name.contains(searchQuery, ignoreCase = true)
//            val matchesDistrict = selectedDistrictId == null ||
//                    school.districtModel.id == selectedDistrictId
//            matchesQuery && matchesDistrict
//        }

    Column(modifier = Modifier.fillMaxSize()) {

        val categories = allCategories

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            FilterChipMenu(
                icon = Icons.Default.FilterList,
                defaultLabel = sortOption.label,
                options = SchoolSortOption.entries,
                selected = sortOption,
                labelTransformer = { "Sort By: ${it.label}" },
                onSelected = onSortOptionChange,
            )

            FilterChipMenu(
                icon = Icons.Outlined.Category,
                defaultLabel = "All Categories",
                options = listOf(ALL_CATEGORY) + categories,
                selected = categories.firstOrNull { it.key == selectedCategory } ?: ALL_CATEGORY,
                labelTransformer = { it.label },
                onSelected = { category ->
                    onCategorySelected(if (category.key.isEmpty()) null else category.key)
                },
            )

            FilterChipMenu(
                icon = Icons.Outlined.Place,
                defaultLabel = "All Districts",
                options = listOf(ALL_DISTRICT) + districts,
                selected = districts.firstOrNull { it.id == selectedDistrictId } ?: ALL_DISTRICT,
                labelTransformer = { it.name },
                onSelected = { district ->
                    onDistrictSelected(if (district.id == ALL_DISTRICT.id) null else district.id)
                },
            )
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
                        onClick = { onSearchQueryChange("") }, modifier = Modifier.size(24.dp)
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

        val filteredSchools = schools

        if (filteredSchools.isEmpty() && !isLoading) {
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
                stickyHeader {
                    Text(
                        text = "Total Schools ${filteredSchools.size}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface).padding(vertical = 8.dp)
                    )

                }
                when (sortOption) {
                    SchoolSortOption.DistrictName -> {
                        val groupedSchools =
                            filteredSchools.sortedBy { it.name }.groupBy { it.districtModel.name }
                                .toList().sortedBy { it.first }

                        groupedSchools.forEach { (group, groupSchools) ->
                            stickyHeader(key = group) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = group,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.weight(1f).fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(vertical = 8.dp)
                                    )

                                    Text(
                                        text = "${groupSchools.size} Schools",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.wrapContentWidth()
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(vertical = 8.dp)
                                    )
                                }

                            }

                            items(groupSchools, key = { it.id }) { school ->
                                SchoolListItem(
                                    school = school,
                                    isSelected = school.id == selectedSchoolId,
                                    onClick = { onSelectSchool(school.id) })
                            }
                        }
                    }

                    SchoolSortOption.SchoolCategory -> {
                        val categoryLabelByKey = allCategories.associate { it.key to it.label }
                        val groupedSchools = filteredSchools.sortedBy { it.name }
                            .groupBy { it.category?.key ?: "Uncategorized" }.toList()
                            .sortedBy { it.first }

                        groupedSchools.forEach { (group, groupSchools) ->
                            stickyHeader(key = group) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = categoryLabelByKey[group] ?: group,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.weight(1f).fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(vertical = 8.dp)
                                    )

                                    Text(
                                        text = "${groupSchools.size} Schools",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.wrapContentWidth()
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(vertical = 8.dp)
                                    )
                                }
                            }

                            items(groupSchools, key = { it.id }) { school ->
                                SchoolListItem(
                                    school = school,
                                    isSelected = school.id == selectedSchoolId,
                                    onClick = { onSelectSchool(school.id) })
                            }
                        }
                    }

                    SchoolSortOption.SchoolName -> {
                        items(filteredSchools.sortedBy { it.name }, key = { it.id }) { school ->
                            SchoolListItem(
                                school = school,
                                isSelected = school.id == selectedSchoolId,
                                onClick = { onSelectSchool(school.id) })
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun <T> FilterChipMenu(
    icon: ImageVector,
    defaultLabel: String,
    options: List<T>,
    selected: T?,
    labelTransformer: (T) -> String,
    onSelected: (T) -> Unit,
) {
    AppDropDownBasic(
        modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(50)),
        options = options,
        labelTransformer = labelTransformer,
        onSelected = onSelected,
        selected = selected,
    ) { currentOption ->
        AppChip(modifier = Modifier.wrapContentWidth()) {
            Row(
                modifier = Modifier.wrapContentWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(icon, "", modifier = Modifier.size(16.dp))
                Text(
                    currentOption?.let(labelTransformer) ?: defaultLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = LocalContentColor.current.copy(alpha = 0.85f),
                )
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
                    }, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    buildString {
                        school.category?.name?.let { append(it) }
                        school.category?.classRange?.let { append(" \u2022 ").append(it) }
                    }, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold
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

private val ALL_DISTRICT = DistrictUiModel(id = -1, name = "All Districts")

private val ALL_CATEGORY = SchoolCategoryUiModel(key = "", name = "All Categories", classRange = "")

private fun filterSchools(
    schools: List<SchoolUiModel>,
    searchQuery: String,
    selectedDistrictId: Int?,
    selectedCategory: String?,
): List<SchoolUiModel> = schools.filter { school ->
    val matchesQuery = searchQuery.isBlank() || school.name.contains(searchQuery, ignoreCase = true)
    val matchesDistrict =
        selectedDistrictId == null || selectedDistrictId == -1 || school.districtModel.id == selectedDistrictId
    val matchesCategory = selectedCategory.isNullOrBlank() || school.category?.key == selectedCategory
    matchesQuery && matchesDistrict && matchesCategory
}
