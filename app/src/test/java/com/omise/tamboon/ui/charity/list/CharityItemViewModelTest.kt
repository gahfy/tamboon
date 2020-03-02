package com.omise.tamboon.ui.charity.list

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.omise.tamboon.model.Charity
import com.omise.tamboon.replacement.testNetworkModule
import com.omise.tamboon.testutils.CoroutinesTestRule
import com.omise.tamboon.ui.charity.CharityViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CharityItemViewModelTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesTestRule()

    private lateinit var parentViewModel: CharityViewModel
    private lateinit var viewModel: CharityItemViewModel

    @Before
    fun setup() {
        startKoin {
            modules(testNetworkModule)
        }

        parentViewModel = CharityViewModel()
        viewModel = CharityItemViewModel(parentViewModel)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun test_call_click_listener_call_parent() {
        val view = mock(View::class.java)
        `when`(view.tag).thenReturn("testTag")

        viewModel.clickListener.onClick(view)
        assertEquals(
            "Parent should be called with same tag",
            "testTag",
            parentViewModel.charityIdToRedirect.value
        )
    }

    @Test
    fun test_bind_charity() {
        val testCharity = Charity(123, "testName", "testLogoUrl")

        viewModel.bind(testCharity)

        assertEquals("binding should update id value", 123, viewModel.id.get())
        assertEquals("binding should update name value", "testName", viewModel.name.get())
        assertEquals("binding should update logoUrl value", "testLogoUrl", viewModel.imageUrl.get())
    }
}