package com.firebaseserviceandroidapp.features.add_note_dialog.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.databinding.NoteDialogItemBinding
import com.firebaseserviceandroidapp.features.add_note_dialog.logic.AddNoteDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddNoteDialogFragment : BottomSheetDialogFragment() {

    private lateinit var viewDataBinding: NoteDialogItemBinding
    private val viewModel: AddNoteDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewDataBinding = NoteDialogItemBinding.inflate(inflater, container, false).apply {
            vmAddNoteDialog = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading) {
                viewDataBinding.AddBottomTodo.visibility = View.GONE
                viewDataBinding.progressBar.visibility = View.VISIBLE
            } else {
                viewDataBinding.AddBottomTodo.visibility = View.VISIBLE
                viewDataBinding.progressBar.visibility = View.GONE
            }
        })

        viewModel.onSuccessCallback = {
            // Reset the form
            viewModel.title.set("")
            viewModel.details.set("")
            // Dismiss the dialog when the note is added successfully

            dismiss()
        }
    }

    private fun initViews() {
        val statusSpinner = viewDataBinding.statusSpinner
        statusSpinner.adapter = viewModel.getStatusAdapter(requireContext())
        statusSpinner.setSelection(1) // Set "In Progress" as the default value

        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val selectedStatus = parent?.getItemAtPosition(position).toString()
                viewModel.status.set(selectedStatus)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewModel.chooseDate.set(
            getString(
                R.string.date_format,
                viewModel.calendar.get(Calendar.DAY_OF_MONTH),
                viewModel.calendar.get(Calendar.MONTH) + 1,
                viewModel.calendar.get(Calendar.YEAR)
            )
        )
        viewDataBinding.ChoseDate.setOnClickListener {
            viewModel.showDatePicker(requireContext()) { date ->
                viewDataBinding.ChoseDate.text = date
            }
        }
    }


}