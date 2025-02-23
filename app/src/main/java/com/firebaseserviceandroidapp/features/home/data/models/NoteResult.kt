package com.firebaseserviceandroidapp.features.home.data.models

import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem

sealed class NoteResult {
    data class Success(val notes: List<NoteItem>) : NoteResult()
    data class Error(val message: String) : NoteResult()
}