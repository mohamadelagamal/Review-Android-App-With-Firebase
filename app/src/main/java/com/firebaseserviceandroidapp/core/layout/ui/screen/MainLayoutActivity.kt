package com.firebaseserviceandroidapp.core.layout.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.example.reviewapisandroid.features.home.logic.MainLayoutViewModel
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.activity.BaseActivity
import com.firebaseserviceandroidapp.core.layout.logic.MainLayoutViewState
import com.firebaseserviceandroidapp.core.utils.LocaleUtil
import com.firebaseserviceandroidapp.databinding.ActivityMainBinding
import com.firebaseserviceandroidapp.features.add_note_dialog.ui.AddNoteDialogFragment
import com.firebaseserviceandroidapp.features.home.ui.screen.HomeFragment
import com.firebaseserviceandroidapp.features.login.ui.screen.LoginActivity
import com.firebaseserviceandroidapp.features.profile.ui.screen.ProfileFragment
import com.firebaseserviceandroidapp.features.search.ui.screen.SearchFragment
import com.firebaseserviceandroidapp.features.setting.ui.screen.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainLayoutActivity : BaseActivity<ActivityMainBinding, MainLayoutViewModel>(),
    MainLayoutViewState {

    private val mainLayoutViewModel: MainLayoutViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    val showDialogBottomSheet = AddNoteDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        binding.vmMainLayout = mainLayoutViewModel
        mainLayoutViewModel.viewState = this

        if (savedInstanceState == null) {
            pushFragment(HomeFragment())
        }

        navigateFragment()
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun makeViewModelProvider(): MainLayoutViewModel {
        return mainLayoutViewModel
    }

    private fun navigateFragment() {
        binding.navigationHome.setOnItemSelectedListener { item ->
            Log.d("HomeLayoutActivity", "Item Selected: ${item.itemId}")
            when (item.itemId) {
                R.id.home -> pushFragment(HomeFragment())
                R.id.Settings -> pushFragment(SettingsFragment())
                R.id.Profile -> pushFragment(ProfileFragment())
                R.id.Search -> pushFragment(SearchFragment())
                else -> false
            }
            true
        }
    }

    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutHome, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun showDialogBottomSheet() {
        showDialogBottomSheet.show(supportFragmentManager, "")
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleUtil.setLocale(newBase!!))
    }
}