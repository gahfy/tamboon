package com.omise.tamboon.ui.success

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.omise.tamboon.R
import com.omise.tamboon.databinding.ActivitySuccessBinding

class SuccessActivity: AppCompatActivity(){
    private lateinit var binding: ActivitySuccessBinding
    private lateinit var viewModel: SuccessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SuccessViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_success)
        binding.viewModel = viewModel

        initObservers()
    }

    private fun initObservers(){
        viewModel.finish.observe(this, Observer { finish ->
            if(finish == true){
                finish()
            }
        })
    }
}