package com.firebaseserviceandroidapp.features.profile.logic

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragmentViewModel
import com.firebaseserviceandroidapp.core.extensions.logD
import com.firebaseserviceandroidapp.features.profile.data.models.ProfileResult
import com.firebaseserviceandroidapp.features.profile.data.models.ProfileUIState
import com.firebaseserviceandroidapp.features.profile.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : BaseFragmentViewModel<ProfileViewState>() {

    private val _profileState = MutableStateFlow<ProfileUIState>(ProfileUIState.Loading)
    val profileState = _profileState.asStateFlow()

    val name = ObservableField<String>("")
    val title = ObservableField<String>("")
    val email = ObservableField<String>("")
    val description = ObservableField<String>("")
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    init {
        fetchUserProfile()
    }

    private fun handleProfileResult(result: ProfileResult) {
        when (result) {
            is ProfileResult.Success -> handleProfileSuccess(result.data)
            is ProfileResult.Error -> handleProfileError(result.error)
        }
    }

    private fun handleProfileSuccess(data: Map<String, Any?>) {
        logD("ProfileFragment", "updateProfile${data["imageUri"]}")
        _profileState.value = ProfileUIState.Success(
            name = data["name"].toString(),
            title = data["title"].toString(),
            email = data["email"].toString(),
            description = data["description"].toString(),
            imageUri = data["imageUri"]?.let { Uri.parse(it.toString()) }
        )
        name.set(data["name"].toString())
        title.set(data["title"].toString())
        email.set(data["email"].toString())
        description.set(data["description"].toString())
        _imageUri.value = data["imageUri"]?.let { Uri.parse(it.toString()) }
    }

    private fun handleProfileError(error: String) {
        _profileState.value = ProfileUIState.Error(error)
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileUIState.Loading
            handleProfileResult(profileRepository.getProfile())
        }
    }

    fun updateProfile(
        newName: String,
        newTitle: String,
        newDescription: String,
        newImageUri: Uri?,
    ) {
        setLoadingDialog(true)
        // check if newImage is null or not
        var newImageUri: Uri? = newImageUri
        if (newImageUri == null) {
            newImageUri = _imageUri.value
        }
        viewModelScope.launch {
            _profileState.value = ProfileUIState.Loading
            handleProfileResult(profileRepository.updateProfile(newName, newTitle, newDescription, newImageUri))
            setLoadingDialog(false)
        }
    }

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }
}