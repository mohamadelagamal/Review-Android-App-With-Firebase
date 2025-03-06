package com.firebaseserviceandroidapp.features.profile.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragment
import com.firebaseserviceandroidapp.databinding.FragmentProfileBinding
import com.firebaseserviceandroidapp.features.profile.data.models.ProfileUIState
import com.firebaseserviceandroidapp.features.profile.logic.ProfileViewModel
import com.firebaseserviceandroidapp.features.profile.logic.ProfileNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(), ProfileNavigator {

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
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun requestReadExternalStoragePermission() {
        val permission =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        permissionLauncher.launch(permission)
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



    private fun showLoadingIndicator() {
        // Show loading indicator
    }

    private fun updateUI(state: ProfileUIState.Success) {
        viewDataBinding.profileName.setText(state.name)
        viewDataBinding.title.setText(state.title)
        viewDataBinding.profileBio.setText(state.description)
        state.imageUri?.let {
            if (isReadExternalStoragePermissionGranted()) {
                Log.e(
                    "ProfileFragment",
                    "updateUI: ${state.imageUri} ${state.imageUri.toString()}"
                )
                viewDataBinding.profileImage.setImageURI(
                    Uri.parse(state.imageUri.toString())
                )
            } else {
                requestReadExternalStoragePermission()
            }
        }
    }

    private fun showError(message: String) {
        viewModel.showMessage(message)
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                viewDataBinding.profileImage.setImageURI(it)
            }
        }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                loadImage()
            } else {
                Log.e("Permission", "User denied the permission.")
                Toast.makeText(
                    requireContext(),
                    "Permission denied! Cannot access gallery.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    private fun loadImage() {
        imagePickerLauncher.launch("image/*")
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