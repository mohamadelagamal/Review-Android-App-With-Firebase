package com.firebaseserviceandroidapp.features.home.ui.screen

import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.fragment.BaseFragment
import com.firebaseserviceandroidapp.databinding.FragmentHomeBinding
import com.firebaseserviceandroidapp.features.home.logic.HomeViewModel
import com.firebaseserviceandroidapp.features.home.logic.HomeViewState
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.firebaseserviceandroidapp.features.home.data.models.NoteListItem
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), HomeViewState {

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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = viewModel.adapter

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            viewModel.adapter.submitList(
                notes.map { note ->
                    NoteListItem.Note(note)
                }
            )
        }
    }

}