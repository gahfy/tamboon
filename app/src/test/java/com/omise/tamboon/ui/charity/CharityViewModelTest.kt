package com.omise.tamboon.ui.charity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.omise.tamboon.replacement.URL_CHARITIES
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

class CharityViewModelTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesTestRule()

    private lateinit var viewModel: CharityViewModel

    @Before
    fun setup() {
        startKoin {
            modules(testNetworkModule)
        }

        viewModel = CharityViewModel()
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun test_get_charities_wrong_url_error_shown() = coroutinesRule.testDispatcher.runBlockingTest {
        URL_CHARITIES = ""
        viewModel.getCharities()
        assertEquals("Error must be shown", true, viewModel.showError.value)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun test_get_charities_good_url_update_adapter() =
        coroutinesRule.testDispatcher.runBlockingTest {
            URL_CHARITIES = "charities_success.json"
            viewModel.getCharities()
            assertEquals("Adapter must be updated", 4, viewModel.adapter.itemCount)
        }

    @Test
    @ExperimentalCoroutinesApi
    fun test_get_charities_loader_update() = coroutinesRule.testDispatcher.runBlockingTest {
        URL_CHARITIES = "charities_success.json"

        assertEquals("Loader must be hidden", false, viewModel.showLoader.get())
        pauseDispatcher()
        viewModel.getCharities()
        assertEquals("Loader must be shown", true, viewModel.showLoader.get())
        runCurrent()
        assertEquals("Loader must be hidden", false, viewModel.showLoader.get())
    }
}