package tech.sumato.kmptemplate.core.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
