package tech.sumato.avn.mp.component.image_viewer

sealed class ImageSource {

    data class Photo(
        val url: String,
        val caption: String? = null,
    ) : ImageSource()

    data class Pano(
        val configUrl: String,
        val caption: String? = null,
    ) : ImageSource()
}