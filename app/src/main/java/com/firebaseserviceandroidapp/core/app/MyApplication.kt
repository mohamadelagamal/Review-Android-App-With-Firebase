package com.firebaseserviceandroidapp.core.app

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.firebaseserviceandroidapp.config.preferences.PreferenceHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()


        val preferenceHelper = PreferenceHelper(this)
        if (preferenceHelper.isDarkModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val localeCode = preferenceHelper.getPreferredLocale()
        Log.e(
            "MyApp",
            "onCreate: localeCode: $localeCode"
        )
    }

}