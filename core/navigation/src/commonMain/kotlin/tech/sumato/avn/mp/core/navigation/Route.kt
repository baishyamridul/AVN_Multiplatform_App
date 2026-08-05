package tech.sumato.avn.mp.core.navigation

object Route {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val FARMER_REGISTRATION = "farmer_registration"
    const val FARMER_DETAILS = "farmer_details/{farmerId}"

    fun farmerDetails(farmerId: String) = "farmer_details/$farmerId"

    const val MAP_ANALYTICS = "map_analytics"

    const val DISTRICT_DASHBOARD = "district-dashboard"

    const val SCHOOL_DASHBOARD = "school_dashboard?districtId={districtId}"

    fun schoolDashboard(districtId: Int?) =
        "school_dashboard?districtId=${districtId ?: -1}"

}
