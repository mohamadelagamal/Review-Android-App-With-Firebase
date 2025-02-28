package com.firebaseserviceandroidapp.features.home.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firebaseserviceandroidapp.databinding.ItemLottieBinding
import com.firebaseserviceandroidapp.databinding.ItemNoteBinding
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem
import com.firebaseserviceandroidapp.features.home.data.models.NoteListItem
import com.firebaseserviceandroidapp.features.home.data.models.NoteListItemDiffCallback

class NoteAdapter(
    private val onDeleteClick: (NoteItem) -> Unit,
) : ListAdapter<NoteListItem, RecyclerView.ViewHolder>(NoteListItemDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_EMPTY = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NoteListItem.Note -> VIEW_TYPE_ITEM
            is NoteListItem.Empty -> VIEW_TYPE_EMPTY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NoteViewHolder(binding, onDeleteClick)

        } else {
            val binding =
                ItemLottieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LottieViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> {
                val item = getItem(position) as NoteListItem.Note
                holder.bind(item.note)
                holder.bindingRoot.delete.setOnClickListener {
                    Log.d("NoteAdapter", "Delete clicked for note: ${item.note.id}")
                    onDeleteClick(item.note)
                    onItemClickListener?.onItemClick(position, item.note)
                }
            }

            is LottieViewHolder -> {
                // No binding necessary; the Lottie layout handles its own animation.
            }
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(pos: Int, noteItem: NoteItem)
    }

    class NoteViewHolder(
        private val binding: ItemNoteBinding,
        private val onDeleteClick: (NoteItem) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        var bindingRoot = binding
        fun bind(noteItem: NoteItem) {
            binding.noteItem = noteItem
            binding.delete.setOnClickListener {
                Log.d("NoteAdapter", "Delete clicked for note: ${noteItem.id}")
                onDeleteClick(noteItem)
            }
            binding.executePendingBindings()
        }
    }

    class LottieViewHolder(binding: ItemLottieBinding) : RecyclerView.ViewHolder(binding.root)
}
