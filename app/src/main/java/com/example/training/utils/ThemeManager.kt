package com.example.training.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)

    fun setDarkMode(isDarkMode: Boolean) {
        prefs.edit().putBoolean(DARK_MODE_KEY, isDarkMode).apply()
        applyTheme(isDarkMode)
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean(DARK_MODE_KEY, false)
    }

    fun applyTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    companion object {
        private const val THEME_PREFS = "theme_preferences"
        private const val DARK_MODE_KEY = "dark_mode"
    }
}