package com.example.reviewapisandroid.features.home.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.reviewapisandroid.features.home.data.repository.MainLayoutRepository
import com.firebaseserviceandroidapp.core.base.activity.BaseViewModel
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem
import com.firebaseserviceandroidapp.core.layout.logic.MainLayoutViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainLayoutViewModel @Inject constructor(private val mainLayoutRepository: MainLayoutRepository) : BaseViewModel<MainLayoutViewState>() {


    private val _newsResponse = MutableLiveData<NoteItem>()
    val noteResponse: LiveData<NoteItem> get() = _newsResponse
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    fun fetchTopHeadlines(category: String, apiKey: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                   // homeRepository.getTopHeadlines(category, apiKey)
                }
              //  _newsResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching top headlines", e)
            }finally {
                _isLoading.value = false
            }
        }
    }

    // set loading state
    fun setLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
    }

    // floating action button click
    fun onFabClick() {
        viewState?.showDialogBottomSheet()
    }
}