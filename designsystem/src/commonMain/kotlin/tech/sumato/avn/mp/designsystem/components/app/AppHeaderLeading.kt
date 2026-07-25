package tech.sumato.avn.mp.designsystem.components.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import avnmultiplatformapp.designsystem.generated.resources.Res
import avnmultiplatformapp.designsystem.generated.resources.app_logo
import org.jetbrains.compose.resources.painterResource


@Composable
fun AppHeaderLeading(modifier: Modifier, leading: @Composable RowScope.() -> Unit) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        leading(this)

        Image(
            painter = painterResource(Res.drawable.app_logo),
            "",
            modifier = Modifier.width(52.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(
                "ARUNACHAL VIDYA NIDHI",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "शिक्षित अरुणाचल, विकसित अरुणाचल",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W500
            )
        }

    }

}