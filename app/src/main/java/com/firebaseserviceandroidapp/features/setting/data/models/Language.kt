package com.firebaseserviceandroidapp.features.setting.data.models

sealed class Language(val code: String, val position: Int) {
    object English : Language("en", 0)
    object Arabic : Language("ar", 1)

    companion object {
        fun fromPosition(position: Int): Language {
            return if (position == 1) Arabic else English
        }
    }
}
