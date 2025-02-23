package com.firebaseserviceandroidapp.features.add_note_dialog.data.model

data class NoteItem(
    val userId: String = "",
    val id: String = "",
    val title: String = "",
    val titleLowercase: String = "",
    val description: String = "",
    val descriptionLowercase: String = "",
    val status: String = "",
    val chooseDate: String = "",
    var onDeleteClick: ((NoteItem) -> Unit)? = null,
    var onEditClick: ((NoteItem) -> Unit)? = null
) {
    constructor() : this("", "", "", "", "", "", "", "")
}
