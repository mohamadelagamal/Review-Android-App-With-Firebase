package com.firebaseserviceandroidapp.features.add_note_dialog.logic

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.widget.ArrayAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.utils.NetworkUtil.createPropertyChangedCallback
import com.firebaseserviceandroidapp.features.add_note_dialog.data.repository.AddNoteDialogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddNoteDialogViewModel @Inject constructor(
    private val application: Application,
    private val addNoteDialogRepository: AddNoteDialogRepository,
) :
    ViewModel(), AddNoteDialogViewState {

    var title = ObservableField<String>()
    var details = ObservableField<String>()
    var status = ObservableField<String>()
    val titleError = ObservableField<String>()
    val detailsError = ObservableField<String>()
    var chooseDate = ObservableField<String>()
    val calendar = Calendar.getInstance()
    val isLoading = MutableLiveData<Boolean>()

    var onSuccessCallback: ((
        chosenDate: String,
    ) -> Unit)? = null

    init {
        title.addOnPropertyChangedCallback(createPropertyChangedCallback(titleError))
        details.addOnPropertyChangedCallback(createPropertyChangedCallback(detailsError))
    }

    fun addNote() {
        if (isFormValid()) {
            isLoading.value = true
            addNoteDialogRepository.addNote(
                title.get() ?: "",
                details.get() ?: "",
                status.get() ?: "",
                chooseDate.get() ?: "",
                onSuccess = {
                    isLoading.value = false
                    onSuccessCallback?.invoke(
                        chooseDate.get().orEmpty(),
                    ) // Invoke the callback meaning the note was added successfully
                },
                onError = { error ->
                    isLoading.value = false
                    handleError(error)
                }
            )
        }
    }

    fun isFormValid(): Boolean {
        var isValid = true

        val titleText = title.get().orEmpty()
        val detailsText = details.get().orEmpty()

        if (titleText.isBlank()) {
            titleError.set(
                application.getString(R.string.todo_title_error)
            )
            isValid = false
        } else {
            titleError.set(null)
        }

        if (detailsText.isBlank()) {
            detailsError.set(
                application.getString(R.string.todo_details_error)
            )
            isValid = false
        } else {
            detailsError.set(null)
        }

        return isValid
    }

    private fun handleError(error: String) {
        // Handle error
    }

    override fun showDatePicker(context: Context, onDateSet: (String) -> Unit) {
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(Calendar.DAY_OF_MONTH, day)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
                val date = "$day/${month + 1}/$year"
                chooseDate.set(date)
                onDateSet(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    override fun getStatusAdapter(context: Context): ArrayAdapter<String> {
        val statusOptions = arrayOf(
            application.getString(R.string.toDo),
            application.getString(R.string.in_progress),
            application.getString(R.string.complete)
        )
        return ArrayAdapter(context, android.R.layout.simple_spinner_item, statusOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }
}