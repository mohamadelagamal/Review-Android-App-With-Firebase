package com.firebaseserviceandroidapp.core.base.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem

abstract class BaseFragmentViewModel<VS> : ViewModel() {

    var viewState: VS? = null

    protected val _messageLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> get() = _messageLiveData

    protected val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> get() = _showLoading

    protected val _showLoadingDialog = MutableLiveData<Boolean>()
    val showLoadingDialog: LiveData<Boolean> get() = _showLoadingDialog

    fun showMessage(message: String) {
        _messageLiveData.value = message
    }

    fun setLoading(isLoading: Boolean) {
        _showLoading.value = isLoading
    }
    fun setLoadingDialog(isLoading: Boolean) {
        _showLoadingDialog.value = isLoading
    }

}