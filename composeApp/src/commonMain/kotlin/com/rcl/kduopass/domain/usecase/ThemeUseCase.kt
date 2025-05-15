package com.rcl.kduopass.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

enum class ThemeMode {
    DARK, LIGHT, NONE
}

class ThemeUseCase @Inject constructor(private val prefs: DataStore<Preferences>) {

    private val themeKey = stringPreferencesKey("theme_mode")

    val currentThemeMode: Flow<ThemeMode> = prefs.data
        .map { preferences ->
            val themeName = preferences[themeKey] ?: ThemeMode.NONE.name
            ThemeMode.valueOf(themeName)
        }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        prefs.edit { settings ->
            settings[themeKey] = themeMode.name
        }
    }
}