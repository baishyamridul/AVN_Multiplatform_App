package tech.sumato.avn.mp.component.panorama.tiles

sealed interface TileState {
    data object NotRequested : TileState
    data object Loading : TileState
    data class Ready(val imageBytes: ByteArray) : TileState {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Ready) return false
            return imageBytes.contentEquals(other.imageBytes)
        }
        override fun hashCode(): Int = imageBytes.contentHashCode()
    }
    data class Failed(val error: Throwable) : TileState
}
