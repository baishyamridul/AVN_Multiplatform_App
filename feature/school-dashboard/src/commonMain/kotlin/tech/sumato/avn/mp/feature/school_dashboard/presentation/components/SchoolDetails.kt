package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PanoramaPhotosphere
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import tech.sumato.avn.mp.designsystem.components.app.AppChip
import tech.sumato.avn.mp.domain.school.model.SchoolImage360Model
import tech.sumato.avn.mp.domain.school.model.SchoolRoomConditionModel
import tech.sumato.avn.mp.feature.school_dashboard.presentation.model.SchoolDetailsUiModel
import coil3.compose.AsyncImage


@Composable
fun SchoolDetails(
    details: SchoolDetailsUiModel,
) {

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
                            category.name,
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

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

        Spacer(modifier = Modifier.height(8.dp))

        if (details.schoolImages.isNotEmpty()) {
            AppCarousel(
                modifier = Modifier.fillMaxWidth(),
                images = details.schoolImages.map { it.large }
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
                        Icon(Icons.Default.PanoramaPhotosphere, "", modifier = Modifier.size(16.dp))
                        Text("Virtual Tour", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            if (details.schoolImages360.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "\uD83D\uDD0E Virtual Tour",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(details.schoolImages360, key = { it.link }) { image ->
                        VirtualTourThumbnail(image = image)
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

        details.classroom?.let { roomCondition ->
            Spacer(Modifier.height(8.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Spacer(Modifier.height(8.dp))

            RoomConditionSection(
                title = "\uD83D\uDDD2\uFE0F Classroom Condition",
                condition = roomCondition
            )
        }

        details.lab?.let { labCondition ->
            Spacer(Modifier.height(8.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            Spacer(Modifier.height(8.dp))

            RoomConditionSection(
                title = "\uD83E\uDDEA Lab Condition",
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

            ProjectsSection(details = details)
        }

        Spacer(Modifier.height(8.dp))

    }

}

@Composable
private fun RoomConditionSection(
    title: String,
    condition: SchoolRoomConditionModel,
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)

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
        Text(
            "\uD83D\uDEEB\uFE0F Facilities",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )

        details.coreFacilities.forEach { facility ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    facility.label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                )
                facility.value?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        if (details.extraFacilities.isNotEmpty()) {
            Text(
                "Extra Facilities",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                details.extraFacilities.take(6).forEach { facility ->
                    AppChip(modifier = Modifier.wrapContentWidth()) {
                        Text(
                            facility.label,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectsSection(details: SchoolDetailsUiModel) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "\uD83C\uDF10 Projects",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )

        details.projects.forEach { project ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    project.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
                androidx.compose.material3.LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    progress = { (project.percent.coerceIn(0, 100) / 100f) },
                    color = Color.Green,
                )
            }
        }
    }
}

@Composable
private fun VirtualTourThumbnail(image: SchoolImage360Model) {
    Column(
        modifier = Modifier.fillMaxWidth().size(width = 120.dp, height = 96.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AsyncImage(
            model = image.thumbnail,
            contentDescription = image.caption,
            modifier = Modifier.fillMaxWidth().height(72.dp),
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
