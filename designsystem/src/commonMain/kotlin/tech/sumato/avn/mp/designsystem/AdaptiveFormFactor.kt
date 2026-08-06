package tech.sumato.avn.mp.designsystem

import androidx.compose.runtime.staticCompositionLocalOf

enum class FormFactor {
    Compact,
    Medium,
    Expanded,
}

val LocalFormFactor = staticCompositionLocalOf { FormFactor.Compact }
