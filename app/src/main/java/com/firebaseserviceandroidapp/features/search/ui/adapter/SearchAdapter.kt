package com.firebaseserviceandroidapp.features.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebaseserviceandroidapp.databinding.ItemLottieBinding
import com.firebaseserviceandroidapp.databinding.ItemSearchResultBinding
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem

class SearchAdapter(private var items: List<NoteItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOTTIE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) VIEW_TYPE_LOTTIE else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SearchViewHolder(binding)
        } else {
            val binding =
                ItemLottieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LottieViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchViewHolder) {
            val note = items[position]
            holder.bind(note)
        }
    }

    override fun getItemCount(): Int {
        return if (items.isEmpty()) 1 else items.size
    }

    fun updateData(newNotes: List<NoteItem>) {
        items = newNotes
        notifyDataSetChanged()
    }

    class SearchViewHolder(
        private val binding: ItemSearchResultBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(noteItem: NoteItem) {
            binding.noteItem = noteItem
            binding.executePendingBindings()
        }
    }

    class LottieViewHolder(
        binding: ItemLottieBinding,
    ) : RecyclerView.ViewHolder(binding.root)
}