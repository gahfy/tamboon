package com.omise.tamboon.ui.donation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.omise.tamboon.R
import com.omise.tamboon.databinding.ActivityDonationBinding
import com.omise.tamboon.ui.success.SuccessActivity
import com.omise.tamboon.utils.AmountFormattingTextWatcher
import com.omise.tamboon.utils.CardExpireFormattingTextWatcher
import com.omise.tamboon.utils.CreditCardFormattingTextWatcher

const val DONATION_ACTIVITY_ARGUMENT_CHARITY_NAME = "charityName"

class DonationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDonationBinding
    private lateinit var viewModel: DonationViewModel
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val charityName = intent.getStringExtra(DONATION_ACTIVITY_ARGUMENT_CHARITY_NAME)

        viewModel = ViewModelProviders.of(this).get(DonationViewModel::class.java)
        viewModel.charityName = charityName?:""

        binding = DataBindingUtil.setContentView(this, R.layout.activity_donation)

        binding.viewModel = viewModel

        initCreditCardWatcher()
        initObservers()

        snackbar = Snackbar.make(binding.lytActivityDonation, R.string.generic_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { viewModel.submitForm() }
    }

    private fun initCreditCardWatcher(){
        binding.txtDonationCreditCard.addTextChangedListener(CreditCardFormattingTextWatcher(binding.txtDonationCreditCard))
        binding.txtDonationAmount.addTextChangedListener(AmountFormattingTextWatcher(binding.txtDonationAmount))
        binding.txtDonationCreditCardExpire.addTextChangedListener(CardExpireFormattingTextWatcher(binding.txtDonationCreditCardExpire))
        binding.txtDonationCreditCardExpire.setOnClickListener {
            binding.txtDonationCreditCardExpire.setSelection(binding.txtDonationCreditCardExpire.text.length)
        }
    }

    private fun initObservers(){
        viewModel.showError.observe(this, Observer { showError ->
            handleError(showError)
        })

        observeTxtLytError(binding.txtlytDonationName, viewModel.nameError)
        observeTxtLytError(binding.txtlytDonationCreditCard, viewModel.creditCardError)
        observeTxtLytError(binding.txtlytDonationCreditCardExpire, viewModel.expirationError)
        observeTxtLytError(binding.txtlytDonationCreditCardCvv, viewModel.cvvError)
        observeTxtLytError(binding.txtlytDonationAmount, viewModel.amountError)
        
        viewModel.redirectToSuccess.observe(this, Observer { redirect ->
            if(redirect == true){
                redirectToSuccess()
            }
        })
    }

    private fun observeTxtLytError(txtlyt: TextInputLayout, errorLiveData: MutableLiveData<Int>){
        errorLiveData.observe(this, Observer { errorResId ->
            if(errorResId != null)
                txtlyt.error = getString(errorResId)
            else
                txtlyt.error = null
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

    private fun redirectToSuccess(){
        val intent = Intent(this, SuccessActivity::class.java)
        startActivity(intent)
        finish()
    }
}