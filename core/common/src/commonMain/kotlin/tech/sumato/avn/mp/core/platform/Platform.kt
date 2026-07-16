package tech.sumato.avn.mp.core.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
