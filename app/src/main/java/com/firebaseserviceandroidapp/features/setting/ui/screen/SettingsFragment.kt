package com.firebaseserviceandroidapp.features.setting.ui.screen

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragment
import com.firebaseserviceandroidapp.databinding.FragmentSettingsBinding
import com.firebaseserviceandroidapp.features.login.ui.screen.LoginActivity
import com.firebaseserviceandroidapp.features.setting.logic.SettingsViewModel
import com.firebaseserviceandroidapp.features.setting.logic.SettingsViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(),
    SettingsViewState {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private var isFirstSelection = true // لمنع الـ Loop عند التهيئة

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.settingsViewModel = viewModel
        viewModel.viewState = this

        viewDataBinding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onDarkModeSwitchClick(isChecked)
        }

        viewDataBinding.spinnerLanguage.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (isFirstSelection) {
                        isFirstSelection = false
                        return
                    }

                    val newLanguage = if (position == 1) "ar" else "en"
                    if (newLanguage != viewModel.getSelectedLanguage()) {
                        viewModel.onLanguageSelected(position)
                        restartApp() // إعادة تشغيل التطبيق بالكامل لضمان تحديث اللغة
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_settings
    }

    override fun makeViewModelProvider(): SettingsViewModel {
        return settingsViewModel
    }

    private fun restartApp() {
        val intent =
            requireActivity().packageManager.getLaunchIntentForPackage(requireActivity().packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireActivity().finish()
        requireActivity().startActivity(intent)
    }

    override fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout)
            .setMessage(R.string.logout_confirmation)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.logout()
                // move to login screen
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    requireActivity(),
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
                startActivity(intent, options.toBundle())
                requireActivity().finish()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

}