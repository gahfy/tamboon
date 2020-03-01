package com.omise.tamboon.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("adapter")
fun RecyclerView.setBindingAdapter(adapter: RecyclerView.Adapter<*>){
    this.adapter = adapter
}