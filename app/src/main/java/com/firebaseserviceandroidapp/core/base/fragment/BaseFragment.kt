package com.firebaseserviceandroidapp.core.base.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.firebaseserviceandroidapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseFragmentViewModel<*>> : Fragment() {
    lateinit var viewDataBinding: DB
    lateinit var viewModel: VM
    private var progressBar: View? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = makeViewModelProvider()
        progressBar = view.findViewById(R.id.progressBar)
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.messageLiveData.observe(viewLifecycleOwner, {
            showAlertDialog(it)
        })

        viewModel.showLoading.observe(viewLifecycleOwner, {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })
        viewModel.showLoadingDialog.observe(viewLifecycleOwner, {
            if (it) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        })
    }

    private fun showAlertDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    protected fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    protected fun hideLoading() {
        progressBar?.visibility = View.GONE
    }
    private fun showLoadingDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    private fun hideLoadingDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }
    abstract fun getLayoutID(): Int
    abstract fun makeViewModelProvider(): VM
}