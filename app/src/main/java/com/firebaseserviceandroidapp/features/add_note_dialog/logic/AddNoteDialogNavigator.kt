package com.firebaseserviceandroidapp.features.add_note_dialog.logic

import android.content.Context
import android.widget.ArrayAdapter

interface AddNoteDialogNavigator {
    fun showDatePicker(context: Context, onDateSet: (String) -> Unit)
    fun getStatusAdapter(context: Context): ArrayAdapter<String>
}