package tech.sumato.avn.mp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Arunachal Vidya Nidhi",
    ) {
        App()
    }
}