package com.firebaseserviceandroidapp.core.extensions

import android.util.Log

fun Any.logD(tag: String = this::class.java.simpleName, message: String) {
    Log.d(tag, message)
}

fun Any.logE(tag: String = this::class.java.simpleName, message: String) {
    Log.e(tag, message)
}

fun Any.logI(tag: String = this::class.java.simpleName, message: String) {
    Log.i(tag, message)
}

fun Any.logW(tag: String = this::class.java.simpleName, message: String) {
    Log.w(tag, message)
}