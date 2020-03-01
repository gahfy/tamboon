package com.omise.tamboon.ui.donation

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omise.tamboon.R
import com.omise.tamboon.extensions.isValidCreditCardNumber
import com.omise.tamboon.model.DonationRequest
import com.omise.tamboon.network.OmiseApi
import com.omise.tamboon.network.TamboonApi
import com.omise.tamboon.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.java.KoinJavaComponent.inject
import java.util.*


class DonationViewModel: ViewModel(){
    private val tamboonApi by inject(TamboonApi::class.java)
    private val omiseApi by inject(OmiseApi::class.java)

    private var currentName = ""
    private var currentCreditCardNumber = ""
    private var currentExpiration = ""
    private var currentCvv = ""
    private var currentAmount = ""

    val showError = SingleLiveEvent<Boolean>()
    val redirectToSuccess = SingleLiveEvent<Boolean>()

    val nameError = MutableLiveData<Int>()
    val creditCardError = MutableLiveData<Int>()
    val expirationError = MutableLiveData<Int>()
    val cvvError = MutableLiveData<Int>()
    val amountError = MutableLiveData<Int>()

    val drawableStartResId = ObservableInt(R.drawable.ic_credit_card_black_24dp)

    val nameTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            nameError.value = null
            currentName = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val creditCardTextWatcher = object: TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            val content = s.toString()
            creditCardError.value = null
            if(content.startsWith("34") || content.startsWith("37")){
                drawableStartResId.set(R.drawable.ic_amex)
            }
            else if(content.startsWith("30") || content.startsWith("36") || content.startsWith("38")){
                drawableStartResId.set(R.drawable.ic_dinersclub)
            }
            else if(content.startsWith("4")){
                drawableStartResId.set(R.drawable.ic_visa)
            }
            else if(content.startsWith("5")){
                drawableStartResId.set(R.drawable.ic_mastercard)
            }
            else if(content.startsWith("6")){
                drawableStartResId.set(R.drawable.ic_discover)
            }
            else{
                drawableStartResId.set(R.drawable.ic_credit_card_black_24dp)
            }
            currentCreditCardNumber = content
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val expireTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            expirationError.value = null
            currentExpiration = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val cvvTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cvvError.value = null
            currentCvv = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    val amountTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            amountError.value = null
            currentAmount = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    var charityName: String = ""
    val submitClickListener = View.OnClickListener {
        submitForm()
    }

    fun submitForm(){
        showError.value = false

        val creditCardDigits = currentCreditCardNumber.replace("[^\\d.]".toRegex(), "")
        val amountDigits = currentAmount.replace("[^\\d.]".toRegex(), "")

        if(currentName.isEmpty()){
            nameError.value = R.string.error_name_not_set
        }

        if(currentCreditCardNumber.isEmpty()){
            creditCardError.value = R.string.error_credit_card_not_set
        }
        else{
            if(creditCardDigits.length != 16){
                creditCardError.value = R.string.error_credit_card_length
            }
            else if(!creditCardDigits.isValidCreditCardNumber()){
                creditCardError.value = R.string.error_credit_card_wrong_number
            }
        }

        if(currentExpiration.isEmpty()){
            expirationError.value = R.string.error_date_not_set
        }
        else if(currentExpiration.length != 5){
            expirationError.value = R.string.error_date_incorrect
        }
        else{
            val dateData = currentExpiration.split("/")
            val monthInt = dateData[0].toInt()
            val yearInt = dateData[1].toInt()

            val calendar = GregorianCalendar()
            calendar.timeInMillis = Date().time

            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR) - 2000

            if(monthInt < 1 || monthInt > 12){
                expirationError.value = R.string.error_date_incorrect
            }

            if(yearInt < year){
                expirationError.value = R.string.error_date_expired
            }
            else if(yearInt == year && monthInt < month){
                expirationError.value = R.string.error_date_expired
            }
            else if(yearInt > year + 10){
                expirationError.value = R.string.error_date_incorrect
            }
        }

        if(currentCvv.isEmpty()){
            cvvError.value = R.string.error_cvv_not_set
        }
        else if(currentCvv.length < 3){
            cvvError.value = R.string.error_cvv_too_short
        }


        if(currentAmount.isEmpty()){
            amountError.value = R.string.error_amount_not_set
        }
        else{
            val amount = amountDigits.toDouble()
            if(amount < 20) {
                amountError.value = R.string.error_amount_zero
            }
        }

        if(nameError.value == null && creditCardError.value == null && expirationError.value == null && cvvError.value == null && amountError.value == null){
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val tokenResult = getToken()

                    val amountInt = (amountDigits.toDouble()*100.0).toInt()

                    val donationRequest = DonationRequest(charityName, tokenResult, amountInt)
                    val donationResult = tamboonApi.donate(donationRequest)
                    if(!donationResult.success){
                        showError.value = true
                    }
                    else{
                        redirectToSuccess.value = true
                    }
                }
                catch(e: Exception){
                    showError.value = true
                }
            }
        }
    }

    private suspend fun getToken(): String{
        val dateData = currentExpiration.split("/")

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("card[name]", currentName)
            .addFormDataPart("card[number]", currentCreditCardNumber.replace("[^\\d.]".toRegex(), ""))
            .addFormDataPart("card[expiration_month]", dateData[0])
            .addFormDataPart("card[expiration_year]", dateData[1])
            .build()

        val token = omiseApi.getToken(requestBody)

        return token.id
    }
}