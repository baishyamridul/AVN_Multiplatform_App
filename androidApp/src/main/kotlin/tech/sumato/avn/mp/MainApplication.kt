package tech.sumato.avn.mp

import android.app.Application
import qrgenerator.AppContext

class MainApplication : Application() {
    companion object {
        lateinit var INSTANCE: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        AppContext.apply { set(applicationContext) }
    }
}