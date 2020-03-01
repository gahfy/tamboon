package com.omise.tamboon.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("url")
fun ImageView.setUrl(url: String){
    Glide.with(this).load(url).into(this)
}