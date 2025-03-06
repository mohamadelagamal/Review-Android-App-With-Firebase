package com.firebaseserviceandroidapp.config.preferences


import android.content.Context
import android.content.SharedPreferences
import com.firebaseserviceandroidapp.core.utils.LocaleUtil

class PreferenceHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val DARK_MODE_KEY = "dark_mode"
        private const val LANGUAGE_POSITION_KEY = "language_position"

    }

    fun setDarkModeEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_MODE_KEY, isEnabled).apply()
    }

    fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(DARK_MODE_KEY, false)
    }

    fun getPreferredLocale(): String {
        return sharedPreferences.getString("preferred_locale", LocaleUtil.OPTION_PHONE_LANGUAGE)!!
    }

    fun setPreferredLocale(localeCode: String) {
        sharedPreferences.edit().putString("preferred_locale", localeCode).apply()
    }
}