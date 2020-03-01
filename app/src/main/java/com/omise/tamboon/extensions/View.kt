package com.omise.tamboon.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("show")
fun View.show(show: Boolean){
    visibility = if(show) View.VISIBLE else View.GONE
}