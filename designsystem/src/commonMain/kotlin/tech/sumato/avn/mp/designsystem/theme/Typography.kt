package tech.sumato.avn.mp.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import avnmultiplatformapp.designsystem.generated.resources.Res
import avnmultiplatformapp.designsystem.generated.resources.GeistMono_Bold
import avnmultiplatformapp.designsystem.generated.resources.GeistMono_Medium
import avnmultiplatformapp.designsystem.generated.resources.GeistMono_Regular
import avnmultiplatformapp.designsystem.generated.resources.GeistMono_SemiBold
import avnmultiplatformapp.designsystem.generated.resources.Geist_Bold
import avnmultiplatformapp.designsystem.generated.resources.Geist_Medium
import avnmultiplatformapp.designsystem.generated.resources.Geist_Regular
import avnmultiplatformapp.designsystem.generated.resources.Geist_SemiBold
import org.jetbrains.compose.resources.Font

@Composable
fun geistFontFamily(): FontFamily = FontFamily(
    Font(Res.font.Geist_Regular, FontWeight.Normal),
    Font(Res.font.Geist_Medium, FontWeight.Medium),
    Font(Res.font.Geist_SemiBold, FontWeight.SemiBold),
    Font(Res.font.Geist_Bold, FontWeight.Bold),
)

@Composable
fun geistMonoFontFamily(): FontFamily = FontFamily(
    Font(Res.font.GeistMono_Regular, FontWeight.Normal),
    Font(Res.font.GeistMono_Medium, FontWeight.Medium),
    Font(Res.font.GeistMono_SemiBold, FontWeight.SemiBold),
    Font(Res.font.GeistMono_Bold, FontWeight.Bold),
)

@Composable
fun appTypography(): Typography {
    val geist = geistFontFamily()
    return Typography(
        displayLarge = TextStyle(fontFamily = geist, fontWeight = FontWeight.Normal, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
        displayMedium = TextStyle(fontFamily = geist, fontWeight = FontWeight.Normal, fontSize = 45.sp, lineHeight = 52.sp, letterSpacing = 0.sp),
        displaySmall = TextStyle(fontFamily = geist, fontWeight = FontWeight.Normal, fontSize = 36.sp, lineHeight = 44.sp, letterSpacing = 0.sp),
        headlineLarge = TextStyle(fontFamily = geist, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = 0.sp),
        headlineMedium = TextStyle(fontFamily = geist, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp, letterSpacing = 0.sp),
        headlineSmall = TextStyle(fontFamily = geist, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp, letterSpacing = 0.sp),
        titleLarge = TextStyle(fontFamily = geist, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp),
        titleMedium = TextStyle(fontFamily = geist, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
        titleSmall = TextStyle(fontFamily = geist, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
        bodyLarge = TextStyle(fontFamily = geist, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
        bodyMedium = TextStyle(fontFamily = geist, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
        bodySmall = TextStyle(fontFamily = geist, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),
        labelLarge = TextStyle(fontFamily = geist, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
        labelMedium = TextStyle(fontFamily = geist, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
        labelSmall = TextStyle(fontFamily = geist, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    )
}
