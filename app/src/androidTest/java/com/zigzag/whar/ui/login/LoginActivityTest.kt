package com.zigzag.whar.ui.login

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import com.zigzag.whar.R
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.action.ViewActions.*
import org.hamcrest.CoreMatchers.*
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.zigzag.whar.ui.profileEdit.ProfileEditActivity

/**
 * Created by salah on 30/12/17.
 */

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule @JvmField
    var intentsTestRule = IntentsTestRule<LoginActivity>(LoginActivity::class.java)

    @Test
    fun checkIfSubmitButtonIsEnablingWhenNumberIsValid() {
        onView(withId(R.id.et_number)).perform(typeText("9895"))
        onView(withId(R.id.btn_submit)).check(matches(not(isEnabled())))
        onView(withId(R.id.et_number)).perform(typeText("340989"))
        onView(withId(R.id.btn_submit)).check(matches(isEnabled()))
        onView(withId(R.id.et_number)).perform(typeText("9895"))
        onView(withId(R.id.btn_submit)).check(matches(not(isEnabled())))
    }

    @Test
    fun checkIfNumberEditTapIsRevertingToNumberField() {
        onView(withId(R.id.s_country_code)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("IN +91"))).perform(click())

        onView(withId(R.id.et_number)).perform(typeText("9747797987"))
        onView(withId(R.id.btn_submit)).perform(click())
        onView(withId(R.id.tv_enter_code)).check(matches(isDisplayed()))
        onView(withText("9747797987 (Tap to edit)")).perform(click())
        onView(withId(R.id.et_number)).check(matches(isDisplayed()))
    }

    @Test
    fun checkVerifyButtonEnableFunctionality() {
        onView(withId(R.id.s_country_code)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("IN +91"))).perform(click())

        onView(withId(R.id.et_number)).perform(typeText("9747797987"))
        onView(withId(R.id.btn_submit)).perform(click())
        onView(withId(R.id.tv_enter_code)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_submit)).check(matches(not(isEnabled())))
        onView(withId(R.id.et_code_1)).perform(typeText("1"))
        onView(withId(R.id.btn_submit)).check(matches(not(isEnabled())))
        onView(withId(R.id.et_code_2)).perform(typeText("1"))
        onView(withId(R.id.btn_submit)).check(matches(not(isEnabled())))
        onView(withId(R.id.et_code_3)).perform(typeText("1"))
        onView(withId(R.id.btn_submit)).check(matches(not(isEnabled())))
        onView(withId(R.id.et_code_4)).perform(typeText("1"))
        onView(withId(R.id.btn_submit)).check(matches(not(isEnabled())))
        onView(withId(R.id.et_code_5)).perform(typeText("1"))
        onView(withId(R.id.btn_submit)).check(matches(not(isEnabled())))
        onView(withId(R.id.et_code_6)).perform(typeText("1"))
        onView(withId(R.id.btn_submit)).check(matches(isEnabled()))
        onView(withId(R.id.et_code_5)).perform(replaceText(""))
        onView(withId(R.id.btn_submit)).check(matches(not(isEnabled())))
    }

    @Test
    fun checkIfDashboardActivityIsLaunchedOnAutoVerification() {
        onView(withId(R.id.s_country_code)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("IN +91"))).perform(click())
        onView(withId(R.id.et_number)).perform(typeText("9895940989"))
        onView(withId(R.id.btn_submit)).perform(click())
        intended(hasComponent(ProfileEditActivity::class.java.name))
    }

    @Test
    fun checkIfDashboardActivityIsLaunchedOnManualVerification() {
        onView(withId(R.id.s_country_code)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("IN +91"))).perform(click())
        onView(withId(R.id.et_number)).perform(typeText("9747797987"))
        onView(withId(R.id.btn_submit)).perform(click())
        onView(withId(R.id.et_code_1)).perform(typeText("1"))
        onView(withId(R.id.et_code_2)).perform(typeText("2"))
        onView(withId(R.id.et_code_3)).perform(typeText("3"))
        onView(withId(R.id.et_code_4)).perform(typeText("4"))
        onView(withId(R.id.et_code_5)).perform(typeText("5"))
        onView(withId(R.id.et_code_6)).perform(typeText("6"))
        onView(withId(R.id.btn_submit)).perform(click())
        intended(hasComponent(ProfileEditActivity::class.java.name))
    }

    @Test
    fun checkIfInvalidCodeErrorIsDisplayed() {
        onView(withId(R.id.s_country_code)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("IN +91"))).perform(click())
        onView(withId(R.id.et_number)).perform(typeText("9747797987"))
        onView(withId(R.id.btn_submit)).perform(click())
        onView(withId(R.id.et_code_1)).perform(typeText("1"))
        onView(withId(R.id.et_code_2)).perform(typeText("2"))
        onView(withId(R.id.et_code_3)).perform(typeText("3"))
        onView(withId(R.id.et_code_4)).perform(typeText("4"))
        onView(withId(R.id.et_code_5)).perform(typeText("5"))
        onView(withId(R.id.et_code_6)).perform(typeText("1"))
        onView(withId(R.id.btn_submit)).check(matches(isEnabled()))
        onView(withId(R.id.btn_submit)).perform(click())
        onView(withText("The verification code entered was invalid")).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfInvalidNumberErrorMessageIsDisplayed() {
        onView(withId(R.id.et_number)).perform(typeText("9*959*+5"))
        onView(withId(R.id.btn_submit)).perform(click())
        onView(withId(R.id.et_number)).check(matches(hasErrorText("Invalid Phone Number")))
        onView(withId(R.id.et_number)).perform(typeText("++"))
        onView(withId(R.id.btn_submit)).perform(click())
        onView(withId(R.id.et_number)).check(matches(hasErrorText("Invalid Phone Number")))
    }

    @Test
    fun checkIfFirebaseErrorMessageIsDisplayed() {
        onView(withId(R.id.s_country_code)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("IN +91"))).perform(click())
        onView(withId(R.id.et_number)).perform(typeText("0000000000"))
        onView(withId(R.id.btn_submit)).perform(click())
        onView(withText("The format of the phone number provided is incorrect")).check(matches(isDisplayed()))
    }
}