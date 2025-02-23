package com.firebaseserviceandroidapp.core.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.firebaseserviceandroidapp.config.preferences.PreferenceHelper
import java.util.Locale

class LocaleUtil {
    companion object {
        val supportedLocales = listOf("en", "ar")
        const val OPTION_PHONE_LANGUAGE = "sys_def"
        fun setLocale(context: Context): Context? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, PreferenceHelper(context).getPreferredLocale())
            } else updateResourcesLegacy(context, PreferenceHelper(context).getPreferredLocale())
        }


        @TargetApi(Build.VERSION_CODES.N)
        private fun updateResources(context: Context, language: String): Context? {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration: Configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        }

        private fun updateResourcesLegacy(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            configuration.locale = locale
            configuration.setLayoutDirection(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }
    }


}