package com.firebaseserviceandroidapp.features.home.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragment
import com.firebaseserviceandroidapp.databinding.FragmentHomeBinding
import com.firebaseserviceandroidapp.features.home.data.models.HomeViewState
import com.firebaseserviceandroidapp.features.home.data.models.NoteListItem
import com.firebaseserviceandroidapp.features.home.logic.HomeNavigator
import com.firebaseserviceandroidapp.features.home.logic.HomeViewModel
import com.firebaseserviceandroidapp.features.home.ui.adapter.NoteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), HomeNavigator {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun getLayoutID(): Int {
        return R.layout.fragment_home
    }

    override fun makeViewModelProvider(): HomeViewModel {
        return homeViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vmHome = viewModel
        viewModel.viewState = this

        viewDataBinding.recyclerView.adapter = viewModel.adapter

    }

    private fun showLoading(isLoading: Boolean) {
        // Implement logic to show/hide loading indicator
    }
}