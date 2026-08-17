package dev.spooky.socialmediaapp.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.spooky.socialmediaapp.data.repository.SessionRepository
import dev.spooky.socialmediaapp.data.util.SessionHelper
import dev.spooky.socialmediaapp.dataStore
import org.koin.core.qualifier.named
import org.koin.dsl.module

val preferencesModule = module {
    single<DataStore<Preferences>> { get<Application>().dataStore }
    single<Preferences.Key<String>>(named("PREFS_AUTH_KEY")) {
        stringPreferencesKey("credentials")
    }
    single<Preferences.Key<String>>(named("PREFS_USER_INFO_KEY")) {
        stringPreferencesKey("user_info")
    }
    single {
        SessionRepository(
            get(named("PREFS_AUTH_KEY")),
            get(named("PREFS_USER_INFO_KEY")),
            get()
        )
    }
    single { SessionHelper(get()) }
}

