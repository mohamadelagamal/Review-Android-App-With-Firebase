package com.firebaseserviceandroidapp.features.home.data.models

import androidx.recyclerview.widget.DiffUtil

class NoteListItemDiffCallback : DiffUtil.ItemCallback<NoteListItem>() {
    override fun areItemsTheSame(oldItem: NoteListItem, newItem: NoteListItem): Boolean {
        return when {
            oldItem is NoteListItem.Note && newItem is NoteListItem.Note ->
                oldItem.note.id == newItem.note.id
            oldItem is NoteListItem.Empty && newItem is NoteListItem.Empty -> true
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: NoteListItem, newItem: NoteListItem): Boolean {
        return oldItem == newItem
    }
}
