package dev.spooky.socialmediaapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dev.spooky.socialmediaapp.di.appModule
import dev.spooky.socialmediaapp.di.dataModule
import dev.spooky.socialmediaapp.di.preferencesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(appModule, dataModule, preferencesModule)
        }
    }
}

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
