package com.firebaseserviceandroidapp.features.home.data.models

import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem

sealed class NoteListItem {
    data class Note(val note: NoteItem) : NoteListItem()
    object Empty : NoteListItem()
}
