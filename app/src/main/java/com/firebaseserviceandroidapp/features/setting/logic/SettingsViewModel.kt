package com.firebaseserviceandroidapp.features.setting.logic

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.viewModelScope
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragmentViewModel
import com.firebaseserviceandroidapp.features.profile.data.repository.ProfileRepository
import com.firebaseserviceandroidapp.features.setting.data.repository.SettingRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    private val settingRepository: SettingRepository,
    private val firebaseAuth: FirebaseAuth,
) : BaseFragmentViewModel<SettingsNavigator>() {

    var isDarkModeEnabled = ObservableField<Boolean>(settingRepository.isDarkModeEnabled())
    var selectedLanguagePosition = ObservableInt(settingRepository.getSelectedLanguage().let {
        when (it) {
            "ar" -> 1
            else -> 0
        }
    })

    @Inject
    lateinit var profileRepository: ProfileRepository

    fun toggleDarkMode(isEnabled: Boolean) {
        isDarkModeEnabled.set(isEnabled)
        viewModelScope.launch {
            settingRepository.setDarkModeEnabled(isEnabled)
            if (isEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    fun onDarkModeSwitchClick(isChecked: Boolean) {
        toggleDarkMode(isChecked)
    }

    fun onLanguageSelected(position: Int) {
        val localeCode = when (position) {
            1 -> "ar"
            else -> "en"
        }
        Log.d("SettingsViewModel", "Language selected: $localeCode")
        setSelectedLanguage(localeCode)
        applyLanguageChange(localeCode)
    }

    private fun setSelectedLanguage(localeCode: String) {
        settingRepository.setSelectedLanguage(localeCode)
    }

    fun getSelectedLanguage(): String {
        return settingRepository.getSelectedLanguage()
    }

    private fun applyLanguageChange(localeCode: String) {
        val locale = Locale(localeCode)
        Locale.setDefault(locale)
        val config = application.resources.configuration
        config.setLocale(locale)
        application.resources.updateConfiguration(config, application.resources.displayMetrics)
    }

    fun showLogoutConfirmationDialog() {
        viewState?.showLogoutConfirmationDialog()
    }

     fun logout() {
        firebaseAuth.signOut()
        profileRepository.clearCache()
        // Navigate to login screen or perform other actions
    }
}