package com.omise.tamboon

import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.omise.tamboon.ui.charity.CharityActivity
import com.omise.tamboon.ui.charity.list.CharityViewHolder
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NeighboursListTest {
    private var mActivity: CharityActivity? = null

    @get:Rule
    var mActivityRule: ActivityTestRule<CharityActivity> = ActivityTestRule(
        CharityActivity::class.java
    )

    @Before
    fun setUp() {
        mActivity = mActivityRule.activity
        assertThat(mActivity, notNullValue())
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    fun test_can_donate() { // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_charity))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CharityViewHolder>(0, click()))

        onView(ViewMatchers.withId(R.id.txt_donation_name)).perform(replaceText("John DOE"))
        onView(ViewMatchers.withId(R.id.txt_donation_credit_card)).perform(replaceText("4242424242424242"))
        onView(ViewMatchers.withId(R.id.txt_donation_credit_card_expire)).perform(replaceText("12/24"))
        onView(ViewMatchers.withId(R.id.txt_donation_credit_card_cvv)).perform(replaceText("1234"))
        onView(ViewMatchers.withId(R.id.txt_donation_amount)).perform(replaceText("30"))
        onView(ViewMatchers.withId(R.id.bt_donation_submit)).perform(click())
        SystemClock.sleep(5000);
        onView(ViewMatchers.withId(R.id.bt_success)).check(matches(withText("DISMISS")))
    }
}
