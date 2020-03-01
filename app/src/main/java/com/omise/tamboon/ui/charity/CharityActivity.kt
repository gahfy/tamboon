package com.omise.tamboon.ui.charity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.omise.tamboon.R
import com.omise.tamboon.databinding.ActivityCharityBinding
import com.omise.tamboon.ui.donation.DONATION_ACTIVITY_ARGUMENT_CHARITY_NAME
import com.omise.tamboon.ui.donation.DonationActivity


class CharityActivity : AppCompatActivity() {
    private lateinit var viewModel: CharityViewModel
    private lateinit var binding: ActivityCharityBinding
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CharityViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_charity)
        binding.viewModel = viewModel

        snackbar = Snackbar.make(binding.lytActivityCharity, R.string.generic_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { viewModel.getCharities() }

        initList()
        initObservers()
    }

    private fun initList(){
        binding.listCharity.layoutManager = LinearLayoutManager(this)
        val horizontalDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val horizontalDivider = ContextCompat.getDrawable(this, R.drawable.horizontal_divider)
        horizontalDecoration.setDrawable(horizontalDivider!!)
        binding.listCharity.addItemDecoration(horizontalDecoration)
    }

    private fun initObservers(){
        viewModel.showError.observe(this, Observer { showError ->
            handleError(showError)
        })

        viewModel.charityIdToRedirect.observe(this, Observer { name ->
            val intent = Intent(this@CharityActivity, DonationActivity::class.java)
            intent.putExtra(DONATION_ACTIVITY_ARGUMENT_CHARITY_NAME, name)
            startActivity(intent)
        })
    }

    private fun handleError(show: Boolean?){
        if(show == true) {
            snackbar.show()
        }
        else{
            snackbar.dismiss()
        }
    }
}