package tech.sumato.avn.mp.component.map

import avnmultiplatformapp.component.map.generated.resources.Res


object GeoJsonLoader {

    suspend fun loadArunachalBoundary(): String {

        return Res.readBytes("files/arunachal_boundary.geojson")
            .decodeToString()

    }

}