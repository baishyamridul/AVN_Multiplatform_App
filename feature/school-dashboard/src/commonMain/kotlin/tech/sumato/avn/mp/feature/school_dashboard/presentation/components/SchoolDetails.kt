package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PanoramaPhotosphere
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Fence
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Signpost
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material.icons.outlined.SolarPower
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Wc
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import qrgenerator.qrkitpainter.PatternType
import qrgenerator.qrkitpainter.QrBallType
import qrgenerator.qrkitpainter.QrFrameType
import qrgenerator.qrkitpainter.QrKitBrush
import qrgenerator.qrkitpainter.QrKitColors
import qrgenerator.qrkitpainter.QrKitShapes
import qrgenerator.qrkitpainter.QrPixelType
import qrgenerator.qrkitpainter.customBrush
import qrgenerator.qrkitpainter.getSelectedFrameShape
import qrgenerator.qrkitpainter.getSelectedPattern
import qrgenerator.qrkitpainter.getSelectedPixel
import qrgenerator.qrkitpainter.getSelectedQrBall
import qrgenerator.qrkitpainter.rememberQrKitPainter
import tech.sumato.avn.mp.designsystem.components.app.AppCarousel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.filled.Vrpano
import androidx.compose.material.icons.outlined.CameraOutdoor
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.PermCameraMic
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Vrpano
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import tech.sumato.avn.mp.designsystem.components.app.AppChip
import tech.sumato.avn.mp.domain.school.model.SchoolImage360Model
import tech.sumato.avn.mp.domain.school.model.SchoolRoomConditionModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolDetailsUiModel
import tech.sumato.avn.mp.component.image_viewer.ImageModal
import tech.sumato.avn.mp.component.image_viewer.ImageSource
import coil3.compose.AsyncImage
import tech.sumato.avn.mp.component.image_viewer.ImageViewer
import tech.sumato.avn.mp.designsystem.components.AppCard
import tech.sumato.avn.mp.designsystem.components.AppCardBordered
import tech.sumato.avn.mp.domain.school.model.SchoolProjectModel
import tech.sumato.avn.mp.domain.school.model.SchoolStaffsModel
import tech.sumato.avn.mp.domain.school.model.SchoolStudentsModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.components.StudentsCountRow


@Composable
fun SchoolDetails(
    details: SchoolDetailsUiModel,
) {

    var modalSource by remember { mutableStateOf<ImageSource?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                details.category?.let { category ->
                    AppChip(modifier = Modifier.wrapContentWidth()) {
                        Text(
                            category.label,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Text(
                    details.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                if (details.udiseCode != null || details.establishedYear != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        details.udiseCode?.let {
                            Text(
                                "UDISE: $it",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        details.establishedYear?.let {
                            Text(
                                "Established: $it",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }

            details.udiseCode?.let { udise ->
                Box(modifier = Modifier.size(80.dp)) {
                    val painter = rememberQrKitPainter("AVN_UDISE_$udise") {
                        shapes = QrKitShapes(
                            ballShape = getSelectedQrBall(QrBallType.RoundCornersQrBall(radius = 0.2f)),
                            darkPixelShape = getSelectedPixel(QrPixelType.RoundCornerPixel()),
                            frameShape = getSelectedFrameShape(QrFrameType.RoundCornersFrame(corner = 0.1f)),
                            codeShape = getSelectedPattern(PatternType.SquarePattern),
                        )
                        colors = QrKitColors(
                            darkBrush = QrKitBrush.customBrush {
                                Brush.linearGradient(
                                    0f to Color.White,
                                    1f to Color.White,
                                    end = Offset(it, it)
                                )
                            }
                        )
                    }

                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))



        if (details.schoolImages.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = Dp.Hairline)

            Spacer(modifier = Modifier.height(8.dp))

            AppCarousel(
                modifier = Modifier.fillMaxWidth().aspectRatio(19f / 9f),
                images = details.schoolImages.map { it.thumbnail },
                caption = { index -> details.schoolImages[index].caption ?: "" },
                onImageClick = { index ->
                    val image = details.schoolImages[index]
                    modalSource = ImageSource.Photo(
                        url = image.large,
                        caption = image.caption,
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.End),
            ) {
                AppChip(modifier = Modifier.wrapContentWidth()) {
                    Row(
                        modifier = Modifier.wrapContentWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(Icons.Default.PhotoAlbum, "", modifier = Modifier.size(16.dp))
                        Text("All photos", style = MaterialTheme.typography.bodySmall)
                    }
                }

                AppChip(modifier = Modifier.wrapContentWidth()) {
                    Row(
                        modifier = Modifier.wrapContentWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(Icons.Default.Vrpano, "", modifier = Modifier.size(16.dp))
                        Text("Virtual Tour", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            if (details.schoolImages360.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                SectionHeading(
                    icon = Icons.Outlined.Vrpano,
                    title = "Virtual Tour"
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(details.schoolImages360, key = { it.link }) { image ->
                        VirtualTourThumbnail(
                            image = image,
                            onClick = {
                                modalSource = ImageSource.Pano(
                                    configUrl = image.link,
                                    caption = image.caption,
                                )
                            },
                        )
                    }
                }
            }
        }

        details.attendance?.let { attendance ->
            Spacer(Modifier.height(8.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Spacer(Modifier.height(8.dp))

            SchoolLiveAttendance(attendance = attendance)
        }

        details.students?.let { schoolStudentsModel ->
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
            Spacer(Modifier.height(8.dp))

            SchoolStudentsSection(students = schoolStudentsModel)

        }

        details.staffs?.let { schoolStaffsModel ->
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
            Spacer(Modifier.height(8.dp))
            SchoolStaffSection(schoolStaffsModel = schoolStaffsModel)
        }

        details.classroom?.let { roomCondition ->
            Spacer(Modifier.height(8.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Spacer(Modifier.height(8.dp))

            RoomConditionSection(
                icon = Icons.Outlined.MeetingRoom,
                title = "Classroom Condition",
                condition = roomCondition
            )
        }

        details.lab?.let { labCondition ->
            Spacer(Modifier.height(8.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Spacer(Modifier.height(8.dp))

            RoomConditionSection(
                icon = Icons.Outlined.Science,
                title = "Lab Condition",
                condition = labCondition
            )
        }


        if (details.coreFacilities.isNotEmpty() || details.extraFacilities.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Spacer(Modifier.height(8.dp))

            FacilitiesSection(details = details)
        }


        if (details.projects.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Spacer(Modifier.height(8.dp))

            ProjectsSection(projects = details.projects)
        }

        Spacer(Modifier.height(8.dp))

    }

//    ImageModal(
//        source = modalSource,
//        onDismissRequest = { modalSource = null },
//    )

    ImageViewer(source = modalSource, onDismissRequest = { modalSource = null })

}

@Composable
fun SectionHeading(
    icon: ImageVector,
    title: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun SchoolStaffSection(schoolStaffsModel: SchoolStaffsModel) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        Text(
//            "\uD83C\uDF93 School Staff",
//            style = MaterialTheme.typography.titleSmall,
//            fontWeight = FontWeight.SemiBold
//        )

        StudentsCountRow(label = "Total Staff", count = schoolStaffsModel.total, percent = null)

    }
}

@Composable
fun SchoolStudentsSection(students: SchoolStudentsModel) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading(
            icon = Icons.Outlined.School,
            title = "Students"
        )

        StudentsCountRow(
            label = "Boys",
            count = students.boys,
            percent = students.getBoysPercent()
        )

        StudentsCountRow(
            label = "Girls",
            count = students.girls,
            percent = students.getGirlsPercent()
        )

        StudentsCountRow(label = "Total", count = students.total, percent = null)

    }
}

@Composable
private fun StudentsCountRow(
    label: String,
    count: Int,
    percent: Double?,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Normal)
        Text(
            buildString {
                append(count)
                if (percent != null) {
                    append(" (").append(percent).append("%)")
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun RoomConditionSection(
    icon: ImageVector,
    title: String,
    condition: SchoolRoomConditionModel,
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading(icon = icon, title = title)

        condition.veryGood?.let {
            RoomCountRow(label = "Very Good", count = it.count, percent = it.percent)
        }
        condition.good?.let {
            RoomCountRow(label = "Good", count = it.count, percent = it.percent)
        }
        condition.poor?.let {
            RoomCountRow(label = "Poor", count = it.count, percent = it.percent)
        }
    }
}

@Composable
private fun RoomCountRow(
    label: String,
    count: Int,
    percent: Double,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Normal)
        Text(
            "$count ($percent%)",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun FacilitiesSection(details: SchoolDetailsUiModel) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading(
            icon = Icons.Outlined.Apartment,
            title = "Facilities"
        )

        if (details.coreFacilities.isNotEmpty()) {
            details.coreFacilities.forEach { facility ->
                val facilityIcon = facilityIcon(facility.key, facility.label)
                FacilityRow(
                    label = facility.label,
                    value = facility.value ?: "-",
                    icon = facilityIcon.icon,
                    iconColor = facilityIcon.color,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (details.extraFacilities.isNotEmpty()) {
            Text(
                "Extra Facilities",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                details.extraFacilities.forEach { facility ->
                    val facilityIcon = facilityIcon("extra", facility.label)
                    FacilityIconChip(
                        label = facility.label,
//                        icon = facilityIcon.icon,
                        color = facilityIcon.color,
                    )
                }
            }
        }
    }
}

@Composable
private fun FacilityRow(
    label: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = RoundedCornerShape(8.dp),
            color = iconColor.copy(alpha = 0.12f),
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(16.dp),
                    tint = iconColor,
                )
            }
        }
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f),
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun FacilityIconChip(
    label: String,
    icon: ImageVector? = null,
    color: Color,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(16.dp),
                tint = color,
            )
        }
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

private data class FacilityIcon(
    val icon: ImageVector,
    val color: Color,
)

val colors = listOf(
    Color(0xfff59e0b),
    Color(0xff06b6d4),
    Color(0xff06b6d4),
    Color(0xff2563eb),
    Color(0xffeab308),
    Color(0xff57534e)
)

private fun facilityIcon(key: String, label: String): FacilityIcon {
    return when (key) {
        "electricity_source" ->
            FacilityIcon(Icons.Outlined.Bolt, Color(0xfff59e0b))

        "water_source" ->
            FacilityIcon(Icons.Outlined.WaterDrop, Color(0xff06b6d4))

        "drinking_water" ->
            FacilityIcon(Icons.Outlined.LocalDrink, Color(0xff06b6d4))

        "internet" ->
            FacilityIcon(Icons.Outlined.Wifi, Color(0xff2563eb))

        "kitchen" -> {
            FacilityIcon(Icons.Outlined.Kitchen, Color(0xffeab308))
        }

        "surveillance" -> {
            FacilityIcon(Icons.Outlined.CameraOutdoor, Color(0xff57534e))
        }

        "extra" -> {
            FacilityIcon(Icons.Outlined.Category, colors.random())
        }


//        "toilet" in text || "wash" in text || "sanitation" in text ->
//            FacilityIcon(Icons.Outlined.Wc, Color(0xff64748b))
//
//        "play" in text || "sport" in text || "ground" in text ->
//            FacilityIcon(Icons.Outlined.SportsSoccer, Color(0xff16a34a))

//        "classroom" in text || "smart" in text || "digital" in text ->
//            FacilityIcon(Icons.Outlined.SmartDisplay, Color(0xff7c3aed))

        "library" ->
            FacilityIcon(Icons.Outlined.Book, Color(0xffb45309))

        "lab" ->
            FacilityIcon(Icons.Outlined.Computer, Color(0xff0d9488))

        "medical" ->
            FacilityIcon(Icons.Outlined.MedicalServices, Color(0xffdc2626))

//        "boundary" in text || "wall" in text || "fence" in text ->
//            FacilityIcon(Icons.Outlined.Fence, Color(0xff4b5563))
//
//        "road" in text || "path" in text || "approach" in text ->
//            FacilityIcon(Icons.Outlined.Signpost, Color(0xff9333ea))

        else ->
            FacilityIcon(Icons.Outlined.Category, Color(0xff6b7280))
    }
}

@Composable
private fun ProjectsSection(projects: List<SchoolProjectModel>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading(
            icon = Icons.Outlined.Construction,
            title = "Projects"
        )

        projects.forEach { project ->
            AppCardBordered {
                Column(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        project.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        minLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        project.status?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                        project.allocatedAmount?.let {
                            Text(
                                "\u20B9 $it",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        progress = { (project.percent.coerceIn(0, 100) / 100f) },
                        color = Color.Green,
                    )
                }
            }
        }
    }
}

@Composable
private fun VirtualTourThumbnail(
    image: SchoolImage360Model,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(width = 120.dp, height = 96.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AsyncImage(
            model = image.thumbnail,
            contentDescription = image.caption,
            modifier = Modifier.fillMaxWidth().height(72.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.FillBounds
        )
        image.caption?.let {
            Text(
                it,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
            )
        }
    }
}
