package tech.sumato.avn.mp.domain.districtDashboard.repository

import tech.sumato.avn.mp.domain.districtDashboard.model.PlaceholderModel

interface PlaceholderRepository {
    suspend fun getData(): List<PlaceholderModel>
}
