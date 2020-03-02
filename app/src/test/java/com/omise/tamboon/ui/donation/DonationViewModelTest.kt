package com.omise.tamboon.ui.donation

import android.text.Editable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.omise.tamboon.R
import com.omise.tamboon.replacement.URL_DONATION
import com.omise.tamboon.replacement.URL_TOKEN
import com.omise.tamboon.replacement.testNetworkModule
import com.omise.tamboon.testutils.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DonationViewModelTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesTestRule()

    private lateinit var viewModel: DonationViewModel

    @Before
    fun setup() {
        startKoin {
            modules(testNetworkModule)
        }

        viewModel = DonationViewModel()
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun test_name_not_set() {
        setNameInViewModel("")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals(
            "Name error should be shown",
            R.string.error_name_not_set,
            viewModel.nameError.value
        )
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_credit_card_not_set() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals(
            "Credit card error should be shown",
            R.string.error_credit_card_not_set,
            viewModel.creditCardError.value
        )
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_credit_card_too_short() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals(
            "Credit card error should be shown",
            R.string.error_credit_card_length,
            viewModel.creditCardError.value
        )
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_credit_card_incorrect() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424243")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals(
            "Credit card error should be shown",
            R.string.error_credit_card_wrong_number,
            viewModel.creditCardError.value
        )
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_expire_date_not_set() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals(
            "Expiration date error should be shown",
            R.string.error_date_not_set,
            viewModel.expirationError.value
        )
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_expire_date_incorrect_month() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("98/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals(
            "Expiration date error should be shown",
            R.string.error_date_incorrect,
            viewModel.expirationError.value
        )
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_expire_date_incorrect_month2() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("0/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals(
            "Expiration date error should be shown",
            R.string.error_date_incorrect,
            viewModel.expirationError.value
        )
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_expire_date_incorrect_length() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("98/4")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals(
            "Expiration date error should be shown",
            R.string.error_date_incorrect,
            viewModel.expirationError.value
        )
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_expire_date_expiration_too_far() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("02/40")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals(
            "Expiration date error should be shown",
            R.string.error_date_incorrect,
            viewModel.expirationError.value
        )
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_expire_date_expired() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("02/16")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals(
            "Expiration date error should be shown",
            R.string.error_date_expired,
            viewModel.expirationError.value
        )
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_expire_date_expired2() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("01/20")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals(
            "Expiration date error should be shown",
            R.string.error_date_expired,
            viewModel.expirationError.value
        )
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_expire_date_same_year_good() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/20")
        setCvvInViewModel("1234")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("Expiration date error should be shown", null, viewModel.expirationError.value)
        assertEquals("No error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_cvv_not_set() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals(
            "CVV error should be shown",
            R.string.error_cvv_not_set,
            viewModel.cvvError.value
        )
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_cvv_too_short() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("12")
        setAmountInViewModel("120")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals(
            "CVV error should be shown",
            R.string.error_cvv_too_short,
            viewModel.cvvError.value
        )
        assertEquals("No error for amount", null, viewModel.amountError.value)
    }

    @Test
    fun test_amount_not_set() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("no error for CVV", null, viewModel.cvvError.value)
        assertEquals(
            "Amount error should be shown",
            R.string.error_amount_not_set,
            viewModel.amountError.value
        )
    }

    @Test
    fun test_amount_less_than_twenty() {
        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("19.99")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("no error for CVV", null, viewModel.cvvError.value)
        assertEquals(
            "Amount error should be shown",
            R.string.error_amount_zero,
            viewModel.amountError.value
        )
    }

    @Test
    fun test_amex_card() {
        setCreditCardNumberInViewModel("34")
        assertEquals(
            "Credit card logo should be shown",
            R.drawable.ic_amex,
            viewModel.drawableStartResId.get()
        )
    }

    @Test
    fun test_amex_card2() {
        setCreditCardNumberInViewModel("37")
        assertEquals(
            "Credit card logo should be shown",
            R.drawable.ic_amex,
            viewModel.drawableStartResId.get()
        )
    }

    @Test
    fun test_diners_club() {
        setCreditCardNumberInViewModel("30")
        assertEquals(
            "Credit card logo should be shown",
            R.drawable.ic_dinersclub,
            viewModel.drawableStartResId.get()
        )
    }

    @Test
    fun test_diners_club2() {
        setCreditCardNumberInViewModel("36")
        assertEquals(
            "Credit card logo should be shown",
            R.drawable.ic_dinersclub,
            viewModel.drawableStartResId.get()
        )
    }

    @Test
    fun test_diners_club3() {
        setCreditCardNumberInViewModel("38")
        assertEquals(
            "Credit card logo should be shown",
            R.drawable.ic_dinersclub,
            viewModel.drawableStartResId.get()
        )
    }

    @Test
    fun test_visa() {
        setCreditCardNumberInViewModel("4")
        assertEquals(
            "Credit card logo should be shown",
            R.drawable.ic_visa,
            viewModel.drawableStartResId.get()
        )
    }

    @Test
    fun test_mastercard() {
        setCreditCardNumberInViewModel("5")
        assertEquals(
            "Credit card logo should be shown",
            R.drawable.ic_mastercard,
            viewModel.drawableStartResId.get()
        )
    }

    @Test
    fun test_discover() {
        setCreditCardNumberInViewModel("6")
        assertEquals(
            "Credit card logo should be shown",
            R.drawable.ic_discover,
            viewModel.drawableStartResId.get()
        )
    }

    @Test
    fun test_unknown_card() {
        setCreditCardNumberInViewModel("9")
        assertEquals(
            "Credit card logo should be shown",
            R.drawable.ic_credit_card_black_24dp,
            viewModel.drawableStartResId.get()
        )
    }

    @Test
    @ExperimentalCoroutinesApi
    fun test_wrong_url_token() = coroutinesRule.testDispatcher.runBlockingTest {
        URL_TOKEN = ""
        URL_DONATION = "donate_success.json"

        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("30")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("no error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)

        assertEquals("Error message should be shown", true, viewModel.showError.value)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun test_good_url_token() = coroutinesRule.testDispatcher.runBlockingTest {

        URL_TOKEN = "token_success.json"
        URL_DONATION = "donate_success.json"

        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("30")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("no error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)

        assertEquals("Error message should not be shown", false, viewModel.showError.value)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun test_wrong_url_donate() = coroutinesRule.testDispatcher.runBlockingTest {

        URL_TOKEN = "token_success.json"
        URL_DONATION = ""

        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("30")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("no error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)

        assertEquals("Error message should be shown", true, viewModel.showError.value)
        assertEquals("Redirection should not be made", null, viewModel.redirectToSuccess.value)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun test_donate_fail() = coroutinesRule.testDispatcher.runBlockingTest {

        URL_TOKEN = "token_success.json"
        URL_DONATION = "donate_fail.json"

        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("30")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("no error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)

        assertEquals("Error message should be shown", true, viewModel.showError.value)
        assertEquals("Redirection should not be made", null, viewModel.redirectToSuccess.value)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun test_donate_success() = coroutinesRule.testDispatcher.runBlockingTest {

        URL_TOKEN = "token_success.json"
        URL_DONATION = "donate_success.json"

        setNameInViewModel("John Doe")
        setCreditCardNumberInViewModel("4242424242424242")
        setExpirationInViewModel("12/24")
        setCvvInViewModel("1234")
        setAmountInViewModel("30")

        viewModel.submitForm()

        assertEquals("No error for name", null, viewModel.nameError.value)
        assertEquals("No error for credit card", null, viewModel.creditCardError.value)
        assertEquals("No error for expiration date", null, viewModel.expirationError.value)
        assertEquals("no error for CVV", null, viewModel.cvvError.value)
        assertEquals("No error for amount", null, viewModel.amountError.value)

        assertEquals("Error message should be shown", false, viewModel.showError.value)
        assertEquals("Redirection should not be made", true, viewModel.redirectToSuccess.value)
    }

    private fun setNameInViewModel(name: String) {

        viewModel.nameTextWatcher.afterTextChanged(mockedEditable(name))
    }

    private fun setCreditCardNumberInViewModel(number: String) {
        viewModel.creditCardTextWatcher.afterTextChanged(mockedEditable(number))
    }

    private fun setExpirationInViewModel(expiration: String) {
        viewModel.expireTextWatcher.afterTextChanged(mockedEditable(expiration))
    }

    private fun setCvvInViewModel(cvv: String) {
        viewModel.cvvTextWatcher.afterTextChanged(mockedEditable(cvv))
    }

    private fun setAmountInViewModel(amount: String) {
        viewModel.amountTextWatcher.afterTextChanged(mockedEditable(amount))
    }

    private fun mockedEditable(value: String): Editable {
        val editable = mock(Editable::class.java)
        `when`(editable.toString()).thenReturn(value)
        return editable
    }
}