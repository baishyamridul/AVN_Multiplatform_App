package tech.sumato.avn.mp.feature.school_dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tech.sumato.avn.mp.domain.school.model.SchoolAttendanceModel
import tech.sumato.avn.mp.domain.school.model.SchoolAttendanceStatusModel


@Composable
fun SchoolLiveAttendance(attendance: SchoolAttendanceModel) {

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeading(
            icon = Icons.Outlined.BarChart,
            title = "Live Attendance (Today)"
        )

        attendance.student?.let { status ->
            Spacer(modifier = Modifier.height(8.dp))

            AttendanceBar(
                title = "Students",
                status = status,
            )
        }

        attendance.staff?.let { status ->
            Spacer(modifier = Modifier.height(16.dp))

            AttendanceBar(
                title = "Faculty",
                status = status,
            )
        }
    }

}

@Composable
private fun AttendanceBar(
    title: String,
    status: SchoolAttendanceStatusModel,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                title, style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
            )
            Text(
                "${status.present} today (${status.percent}%)",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth().height(6.dp),
            progress = { (status.percent.coerceIn(0.0, 100.0) / 100).toFloat() },
            trackColor = Color.LightGray,
            color = Color.Green,
        )
    }
}