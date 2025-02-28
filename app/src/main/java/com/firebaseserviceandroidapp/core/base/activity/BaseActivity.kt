package com.firebaseserviceandroidapp.core.base.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.firebaseserviceandroidapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel<*>> : AppCompatActivity() {
    lateinit var viewDataBinding: DB
    lateinit var viewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutID())
        viewModel = makeViewModelProvider()
        subscribeToLiveData()
    }

    lateinit var view: View
    private fun subscribeToLiveData() {
        viewModel.messageLiveData.observe(this, {
            showAlertDialog(it)
        })


        viewModel.showLoading.observe(this, {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })

    }

    private fun showAlertDialog(message: String) {
        MaterialAlertDialogBuilder(this).setMessage(message).setPositiveButton(this?.getString(R.string.yes))
        { dialog, which ->
            dialog.dismiss()
        }.show()
    }


    var progressDialog: ProgressDialog? = null
    private fun showLoading() {
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("${getString(R.string.loading)}...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    private fun hideLoading() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    abstract fun getLayoutID(): Int
    abstract fun makeViewModelProvider(): VM

}