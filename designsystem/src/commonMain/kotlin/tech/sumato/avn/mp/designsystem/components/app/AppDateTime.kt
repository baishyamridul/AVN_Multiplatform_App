package tech.sumato.avn.mp.designsystem.components.app

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


@Composable
fun AppDateTime(modifier: Modifier) {

    var currentTime by remember {
        mutableStateOf(Clock.System.now())
    }

    var showColon by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Clock.System.now()
            showColon = !showColon
            delay(1000)
        }
    }

    val localDateTime = currentTime.toLocalDateTime(
        TimeZone.currentSystemDefault()
    )

    val date = localDateTime.date
    val time = localDateTime.time
    val month = date.month.name
        .take(3)
        .lowercase()
        .replaceFirstChar { it.uppercase() }

    // Convert 24-hour → 12-hour
    val hour = when {
        time.hour == 0 -> 12
        time.hour > 12 -> time.hour - 12
        else -> time.hour
    }

    val amPm = if (time.hour < 12) "AM" else "PM"

    val colon = if (showColon) ":" else " "

    val currentDateTime = buildString {
        append(date.day)
        append(" ")
        append(month)
        append(" ")
        append(date.year)
        append("  ")
        append(hour.toString().padStart(2, '0'))
        append(colon)
        append(time.minute.toString().padStart(2, '0'))
        append(" ")
        append(amPm)
    }

    Text(
        text = currentDateTime,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.W500
    )

}