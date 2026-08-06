package tech.sumato.avn.mp.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import avnmultiplatformapp.designsystem.generated.resources.GeistMono_Bold
import avnmultiplatformapp.designsystem.generated.resources.GeistMono_Medium
import avnmultiplatformapp.designsystem.generated.resources.GeistMono_Regular
import avnmultiplatformapp.designsystem.generated.resources.GeistMono_SemiBold
import avnmultiplatformapp.designsystem.generated.resources.Geist_Bold
import avnmultiplatformapp.designsystem.generated.resources.Geist_Medium
import avnmultiplatformapp.designsystem.generated.resources.Geist_Regular
import avnmultiplatformapp.designsystem.generated.resources.Geist_SemiBold
import avnmultiplatformapp.designsystem.generated.resources.Res
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