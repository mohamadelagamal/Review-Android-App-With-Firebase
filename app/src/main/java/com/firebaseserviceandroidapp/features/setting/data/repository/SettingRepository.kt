package com.firebaseserviceandroidapp.features.setting.data.repository

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.firebaseserviceandroidapp.config.preferences.PreferenceHelper
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val preferenceHelper: PreferenceHelper,
) {


    fun setDarkModeEnabled(isEnabled: Boolean) {
        preferenceHelper.setDarkModeEnabled(isEnabled)
    }

    fun isDarkModeEnabled(): Boolean {
        return preferenceHelper.isDarkModeEnabled()
    }

    fun setSelectedLanguage(localeCode: String) {
        preferenceHelper.setPreferredLocale(localeCode)

    }

    fun getSelectedLanguage(): String {
        return preferenceHelper.getPreferredLocale()
    }
}