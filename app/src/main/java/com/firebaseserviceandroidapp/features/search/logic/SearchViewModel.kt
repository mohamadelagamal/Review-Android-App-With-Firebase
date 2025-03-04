package com.firebaseserviceandroidapp.features.search.logic

import android.app.Application
import android.util.Log
import android.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragmentViewModel
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem
import com.firebaseserviceandroidapp.features.search.data.repository.SearchRepository
import com.firebaseserviceandroidapp.features.search.ui.adapter.SearchAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private var searchRepository: SearchRepository,
    private var application: Application
) : BaseFragmentViewModel<SearchNavigator>() {

    private val _searchResults = MutableLiveData<List<NoteItem>>()
    val searchResults: LiveData<List<NoteItem>> = _searchResults
    val notesAdapter: SearchAdapter = SearchAdapter(listOf())
    fun search(query: String) {
        viewModelScope.launch {
            val results = searchRepository.searchItems(query)
            Log.e("SearchViewModel", "search: $results")
            notesAdapter.updateData(results)
            _searchResults.postValue(results)
        }
    }
    // set up search view listener here to call search function when text is changed or submitted
    fun setUpSearchViewListener() {
        viewState?.getSearchView()?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText ?: "")
                return true
            }
        })
    }

}