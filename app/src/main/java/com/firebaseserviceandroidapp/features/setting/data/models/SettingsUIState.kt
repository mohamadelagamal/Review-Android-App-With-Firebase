package com.firebaseserviceandroidapp.features.setting.data.models

data class SettingsUIState(
    val isDarkModeEnabled: Boolean = false,
    val selectedLanguage: Language = Language.English,
)
