package com.zigzag.whar.ui.profileEdit

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.PickerActions
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.zigzag.whar.R
import com.zigzag.whar.ui.dashboard.DashboardActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.widget.DatePicker
import android.support.test.espresso.matcher.ViewMatchers.withClassName
import android.support.test.espresso.Espresso.onView
import org.hamcrest.Matchers


/**
 * Created by salah on 19/1/18.
 */

@RunWith(AndroidJUnit4::class)
class ProfileEditActivityTest {

    @Rule
    @JvmField
    var intentsTestRule = IntentsTestRule<ProfileEditActivity>(ProfileEditActivity::class.java)

    @Test
    fun verifyButtonValidationAndNavigationOnSuccess() {
        onView(withId(R.id.btn_submit)).check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))
        onView(withId(R.id.et_first_name)).perform(ViewActions.typeText("sal"))
        onView(withId(R.id.et_last_name)).perform(ViewActions.typeText("sam"), closeSoftKeyboard())
        onView(withId(R.id.et_dob)).perform(click())
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(PickerActions.setDate(1993, 5, 28))
        onView(withText("OK")).perform(click())
        onView(withId(R.id.btn_submit)).check(ViewAssertions.matches(isEnabled()))
        onView(withId(R.id.btn_submit)).perform(ViewActions.click())
        intended(hasComponent(DashboardActivity::class.java.name))
    }
}
