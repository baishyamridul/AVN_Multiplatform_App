package tech.sumato.avn.mp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.maplibre.compose.desktop.DesktopRuntimeOptions
import org.maplibre.compose.desktop.MapLibre
import org.maplibre.compose.desktop.ProvideMapHost
import org.maplibre.compose.desktop.desktopCachePath
import org.maplibre.compose.desktop.rememberAwtComposeGpuHost

fun main() {
    MapLibre.configure(
        DesktopRuntimeOptions(
            cachePath = desktopCachePath("tech.sumato.avn.mp"),
        )
    )
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Arunachal Vidya Nidhi",
        ) {
            ProvideMapHost(host = rememberAwtComposeGpuHost(window)) {
                App()
            }
        }
    }
}
