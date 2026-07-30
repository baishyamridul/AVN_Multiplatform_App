rootProject.name = "AVNMultiplatformApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://jogamp.org/deployment/maven")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":androidApp")
include(":desktopApp")
include(":shared")
//include(":webApp")

include(":core:common")
include(":core:network")
include(":core:navigation")
include(":core:datastore")
include(":domain:user")
include(":data:user")
include(":domain:districtDashboard")
include(":feature:login")
include(":designsystem")
include(":domain:dashboard")
include(":data:dashboard")
include(":data:districtDashboard")
include(":feature:dashboard")
include(":feature:district-dashboard")



include(":component:map")
include(":feature:map-analytics")


include(":feature:school-dashboard")
include(":component:image360")

include(":data:districtDashboard")
