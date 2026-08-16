package dev.spooky.socialmediaapp

import android.app.Application
import dev.spooky.socialmediaapp.di.appModule
import dev.spooky.socialmediaapp.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(appModule, dataModule)
        }
    }
}