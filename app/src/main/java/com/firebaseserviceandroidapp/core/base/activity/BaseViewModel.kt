package com.firebaseserviceandroidapp.core.base.activity


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<N>:ViewModel(){
    var viewState:N?=null
    val showLoading = MutableLiveData<Boolean>()
    val messageLiveData = MutableLiveData<String>()

}