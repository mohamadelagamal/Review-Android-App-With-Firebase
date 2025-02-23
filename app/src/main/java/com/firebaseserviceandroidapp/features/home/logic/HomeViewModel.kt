package com.firebaseserviceandroidapp.features.home.logic

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragmentViewModel
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem
import com.firebaseserviceandroidapp.features.home.data.models.NoteListItem
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
    application: Application,
) : BaseFragmentViewModel<HomeViewState>() {

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
    val adapter = NoteAdapter { noteListItem ->
        if (noteListItem is NoteListItem.Note) {
            deleteNoteItem(noteListItem.note)
        }
    }

    init {
        loadNotes(_selectedDate.value!!)
    }

    fun loadNotes(date: String) {
        _selectedDate.value = date
        viewModelScope.launch(Dispatchers.Main) {
            homeRepository.getNotesRealTime(date).collect { result ->
                when (result) {
                    is com.firebaseserviceandroidapp.features.home.data.models.NoteResult.Success -> {
                        _notes.value = result.notes
                        val listItems = if (result.notes.isEmpty()) {
                            listOf(NoteListItem.Empty)
                        } else {
                            result.notes.map { NoteListItem.Note(it) }
                        }
                        adapter.submitList(listItems)
                    }

                    is com.firebaseserviceandroidapp.features.home.data.models.NoteResult.Error -> {
                        showMessage("Error: ${result.message}")
                    }
                    // Optionally handle loading state if needed.
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
        viewModelScope.launch(Dispatchers.Main) {
            when (val result = homeRepository.deleteNoteById(noteItem.id)) {
                is com.firebaseserviceandroidapp.features.home.data.models.NoteResult.Success -> {
                    val updatedNotes = _notes.value?.filter { it.id != noteItem.id } ?: emptyList()
                    _notes.value = updatedNotes
                    val listItems = if (updatedNotes.isEmpty()) {
                        listOf(NoteListItem.Empty)
                    } else {
                        updatedNotes.map { NoteListItem.Note(it) }
                    }
                    adapter.submitList(listItems)
                    showMessage("Note deleted successfully")
                }

                is com.firebaseserviceandroidapp.features.home.data.models.NoteResult.Error -> {
                    showMessage("Failed to delete note: ${result.message}")
                }
            }
        }
    }
}
