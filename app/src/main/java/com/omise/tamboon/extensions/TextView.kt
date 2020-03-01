package com.omise.tamboon.extensions

import android.text.TextWatcher
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("textWatcher")
fun TextView.addBindingTextWatcher(textWatcher: TextWatcher){
    addTextChangedListener(textWatcher)
}

@BindingAdapter("drawableStartRes")
fun TextView.setDrawableStartResId(drawableStart: Int){
    setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart, 0, 0, 0)
}