package com.udacity.project4.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Context.showToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(message:String){
    Toast.makeText(this.requireContext(),message,Toast.LENGTH_LONG).show()
}