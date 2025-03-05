package com.firebaseserviceandroidapp.features.search.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.viewModels
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragment
import com.firebaseserviceandroidapp.databinding.FragmentSearchBinding
import com.firebaseserviceandroidapp.features.search.logic.SearchViewModel
import com.firebaseserviceandroidapp.features.search.logic.SearchViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(), SearchViewState {

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.searchViewModel = viewModel
        viewModel.viewState = this
        viewModel.setUpSearchViewListener()
    }
    override fun getLayoutID(): Int {
        return R.layout.fragment_search
    }

    override fun makeViewModelProvider(): SearchViewModel {
        return searchViewModel;
    }

    override fun getSearchView(): SearchView {
        return viewDataBinding.searchView
    }
}