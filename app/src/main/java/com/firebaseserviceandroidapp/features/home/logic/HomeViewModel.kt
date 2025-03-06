package com.firebaseserviceandroidapp.features.home.logic

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragmentViewModel
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem
import com.firebaseserviceandroidapp.features.home.data.models.HomeViewState
import com.firebaseserviceandroidapp.features.home.data.models.NoteListItem
import com.firebaseserviceandroidapp.features.home.data.models.NoteResult
import com.firebaseserviceandroidapp.features.home.data.repository.HomeRepository
import com.firebaseserviceandroidapp.features.home.ui.adapter.NoteAdapter
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    val application: Application,
) : BaseFragmentViewModel<HomeNavigator>() {

    private val _homeViewState = MutableLiveData<HomeViewState>()
    val homeViewState: LiveData<HomeViewState> = _homeViewState
    private val _notes = MutableLiveData<List<NoteItem>>()
    val notes: LiveData<List<NoteItem>> = _notes

    private val _selectedDate = MutableLiveData<String>().apply {
        val date = CalendarDay.today()
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.getDefault())
        value = date.date.format(formatter)
    }
    val selectedDate: LiveData<String> = _selectedDate

    // Expose calendarDay for data binding
    private val _calendarDay = MutableLiveData<CalendarDay>().apply { value = CalendarDay.today() }
    val calendarDay: LiveData<CalendarDay> = _calendarDay

    // Update adapter to work with sealed class NoteListItem.
    // onDeleteClick will only be executed if the item is a Note.
    val adapter = NoteAdapter(
        onDeleteClick = { noteItem ->
            deleteNoteItem(noteItem)
        }
    )

    init {
        loadNotes(_selectedDate.value!!)
    }

    fun loadNotes(date: String) {
        _selectedDate.value = date
        _homeViewState.value = HomeViewState.Loading  // Set Loading state

        viewModelScope.launch(Dispatchers.Main) {
            homeRepository.getNotesRealTime(date).collect { result ->
                _homeViewState.value = when (result) {
                    is NoteResult.Success -> {
                        val listItems = if (result.notes.isEmpty()) {
                            listOf(NoteListItem.Empty)
                        } else {
                            result.notes.map { NoteListItem.Note(it) }
                        }
                        adapter.submitList(listItems)
                        _notes.value = result.notes
                        HomeViewState.Success(result.notes)
                    }

                    is NoteResult.Error -> {
                        HomeViewState.Error(result.message)
                    }
                }
            }
        }
    }

    fun onDateSelected(date: CalendarDay) {
        _calendarDay.value = date
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.getDefault())
        val formattedDate = date.date.format(formatter)
        _selectedDate.value = formattedDate
        loadNotes(formattedDate)
    }


    fun deleteNoteItem(noteItem: NoteItem) {
        Log.d("HomeViewModel", "Attempting to delete note: ${noteItem.id}")

        viewModelScope.launch(Dispatchers.Main) {
            when (val result = homeRepository.deleteNoteById(noteItem.id)) {
                is NoteResult.Success -> {
                    Log.d("HomeViewModel", "Note deleted successfully, reloading notes...")
                    loadNotes(_selectedDate.value!!) // Reload notes after deletion
//                    showMessage(application.getString(R.string.note_deleted_successfully))
                }

                is NoteResult.Error -> {
                    Log.e("HomeViewModel", "Failed to delete note: ${result.message}")
                    showMessage("${application.getString(R.string.failed_to_delete_note)}: ${result.message}")
                }
            }
        }
    }
}

