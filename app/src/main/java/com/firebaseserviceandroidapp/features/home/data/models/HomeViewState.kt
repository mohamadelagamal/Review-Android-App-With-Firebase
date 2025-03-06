package com.firebaseserviceandroidapp.features.home.data.models

import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem

sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Success(val notes: List<NoteItem>) : HomeViewState()
    data class Error(val message: String) : HomeViewState()
}