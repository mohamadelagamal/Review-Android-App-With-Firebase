package com.firebaseserviceandroidapp.features.profile.ui.screen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragment
import com.firebaseserviceandroidapp.databinding.FragmentProfileBinding
import com.firebaseserviceandroidapp.features.profile.data.models.ProfileUIState
import com.firebaseserviceandroidapp.features.profile.logic.ProfileViewModel
import com.firebaseserviceandroidapp.features.profile.logic.ProfileViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(), ProfileViewState {

    private val profileViewModel: ProfileViewModel by viewModels()
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_READ_EXTERNAL_STORAGE = 2
    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.profileViewModel = profileViewModel
        profileViewModel.viewState = this

        setupClickListeners()
        observeProfileState()
        observeImageUri()
    }

    private fun setupClickListeners() {
        viewDataBinding.profileImage.setOnClickListener {
            handleProfileImageClick()
        }

        viewDataBinding.updateButton.setOnClickListener {
            handleUpdateButtonClick()
        }
    }

    private fun handleProfileImageClick() {
        if (isReadExternalStoragePermissionGranted()) {
            loadImage()
        } else {
            requestReadExternalStoragePermission()
        }
    }

    private fun handleUpdateButtonClick() {
        profileViewModel.updateProfile(
            viewDataBinding.profileName.text.toString(),
            viewDataBinding.title.text.toString(),
            viewDataBinding.profileBio.text.toString(),
            imageUri
        )
    }

    private fun isReadExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_READ_EXTERNAL_STORAGE
        )
    }

    private fun observeProfileState() {
        lifecycleScope.launchWhenStarted {
            profileViewModel.profileState.collect { state ->
                when (state) {
                    is ProfileUIState.Loading -> showLoadingIndicator()
                    is ProfileUIState.Success -> updateUI(state)
                    is ProfileUIState.Error -> showError(state.message)
                }
            }
        }
    }

    private fun observeImageUri() {
        profileViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            if (isReadExternalStoragePermissionGranted()) {
                viewDataBinding.profileImage.setImageURI(uri)
            } else {
                requestReadExternalStoragePermission()
            }
        }
    }

    private fun showLoadingIndicator() {
        // Show loading indicator
    }

    private fun updateUI(state: ProfileUIState.Success) {
        viewDataBinding.profileName.setText(state.name)
        viewDataBinding.title.setText(state.title)
        viewDataBinding.profileBio.setText(state.description)
        state.imageUri?.let {
            if (isReadExternalStoragePermissionGranted()) {
                viewDataBinding.profileImage.setImageURI(it)
            } else {
                requestReadExternalStoragePermission()
            }
        }
    }

    private fun showError(message: String) {
        viewModel.showMessage(message)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            viewDataBinding.profileImage.setImageURI(imageUri)
            profileViewModel.setImageUri(imageUri)
        }
    }

    private fun loadImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadImage()
        }
    }

    override fun getLayoutID(): Int = R.layout.fragment_profile

    override fun makeViewModelProvider(): ProfileViewModel = profileViewModel
}