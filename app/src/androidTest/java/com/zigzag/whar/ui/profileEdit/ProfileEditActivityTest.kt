package com.zigzag.whar.ui.profileEdit

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.assertion.ViewAssertions
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
        onView(withId(R.id.et_first_name)).perform(ViewActions.typeText("sapl"))
        onView(withId(R.id.et_last_name)).perform(ViewActions.typeText("sam"), closeSoftKeyboard())
        onView(withId(R.id.et_dob)).perform(click())
        onView(withId(R.id.btn_submit)).check(ViewAssertions.matches(isEnabled()))
        onView(withId(R.id.btn_submit)).perform(ViewActions.click())
        intended(hasComponent(DashboardActivity::class.java.name))

/*
        val appCompatEditText5 = onView(
                allOf<View>(withId(R.id.et_first_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_first_name),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText5.perform(replaceText("Sal"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
                allOf<View>(withId(R.id.et_first_name), withText("Sal"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_first_name),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText6.perform(pressImeActionButton())

        val appCompatEditText7 = onView(
                allOf<View>(withId(R.id.et_last_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_last_name),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText7.perform(replaceText("Sam"), closeSoftKeyboard())

        val appCompatEditText8 = onView(
                allOf<View>(withId(R.id.et_last_name), withText("Sam"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_last_name),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText8.perform(click())

        val appCompatEditText9 = onView(
                allOf<View>(withId(R.id.et_last_name), withText("Sam"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_last_name),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText9.perform(pressImeActionButton())

        val appCompatTextView = onView(
                allOf<View>(withClassName(`is`<String>("android.support.v7.widget.AppCompatTextView")), withText("2018"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`<String>("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatTextView.perform(click())

        val appCompatTextView2 = onData(anything())
                .inAdapterView(allOf<View>(withClassName(`is`<String>("android.widget.YearPickerView")),
                        childAtPosition(
                                withClassName(`is`<String>("com.android.internal.widget.DialogViewAnimator")),
                                1)))
                .atPosition(93)
        appCompatTextView2.perform(click())

        val appCompatButton2 = onView(
                allOf<View>(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`<String>("android.widget.ScrollView")),
                                        0),
                                3)))
        appCompatButton2.perform(scrollTo(), click())

        val appCompatEditText10 = onView(
                allOf<View>(withId(R.id.et_dob), withText("19 Jan 1993"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_dob),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText10.perform(click())

        val appCompatTextView3 = onView(
                allOf<View>(withClassName(`is`<String>("android.support.v7.widget.AppCompatTextView")), withText("2018"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`<String>("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatTextView3.perform(click())

        val appCompatTextView4 = onData(anything())
                .inAdapterView(allOf<View>(withClassName(`is`<String>("android.widget.YearPickerView")),
                        childAtPosition(
                                withClassName(`is`<String>("com.android.internal.widget.DialogViewAnimator")),
                                1)))
                .atPosition(93)
        appCompatTextView4.perform(click())

        val appCompatTextView5 = onView(
                allOf<View>(withClassName(`is`<String>("android.support.v7.widget.AppCompatTextView")), withText("Tue, 19 Jan"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`<String>("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()))
        appCompatTextView5.perform(click())

        val appCompatTextView6 = onView(
                allOf<View>(withClassName(`is`<String>("android.support.v7.widget.AppCompatTextView")), withText("Tue, 19 Jan"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`<String>("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()))
        appCompatTextView6.perform(click())

        val appCompatButton3 = onView(
                allOf<View>(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`<String>("android.widget.ScrollView")),
                                        0),
                                3)))
        appCompatButton3.perform(scrollTo(), click())

        onView(withId(R.id.btn_submit)
                        isDisplayed()))
        circularProgressButton.perform(click())*/
    }
}