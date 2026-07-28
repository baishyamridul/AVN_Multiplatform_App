package tech.sumato.avn.mp.component.panorama.tiles

enum class CubeFace(val id: String, val axis: Int, val sign: Int) {
    RIGHT("r", 0, 1),
    LEFT("l", 0, -1),
    UP("u", 1, 1),
    DOWN("d", 1, -1),
    FRONT("f", 2, 1),
    BACK("b", 2, -1);

    companion object {
        private val map = entries.associateBy { it.id }
        fun fromId(id: String): CubeFace = map[id] ?: error("Unknown face id: $id")
    }
}
