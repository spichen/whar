package com.zigzag.whar.ui.dashboard;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.zigzag.whar.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DashboardActivityTest {

    @Rule
    public ActivityTestRule<DashboardActivity> mActivityTestRule = new ActivityTestRule<>(DashboardActivity.class);

    @Test
    public void dashboardActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3596520);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_number),
                        childAtPosition(
                                allOf(withId(R.id.llc_number_container),
                                        childAtPosition(
                                                withId(R.id.cl_number_input_container),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_number),
                        childAtPosition(
                                allOf(withId(R.id.llc_number_container),
                                        childAtPosition(
                                                withId(R.id.cl_number_input_container),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("989"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.et_number), withText("989"),
                        childAtPosition(
                                allOf(withId(R.id.llc_number_container),
                                        childAtPosition(
                                                withId(R.id.cl_number_input_container),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.et_number), withText("989"),
                        childAtPosition(
                                allOf(withId(R.id.llc_number_container),
                                        childAtPosition(
                                                withId(R.id.cl_number_input_container),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("9895940989"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.et_number), withText("9895940989"),
                        childAtPosition(
                                allOf(withId(R.id.llc_number_container),
                                        childAtPosition(
                                                withId(R.id.cl_number_input_container),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.et_number), withText("9895940989"),
                        childAtPosition(
                                allOf(withId(R.id.llc_number_container),
                                        childAtPosition(
                                                withId(R.id.cl_number_input_container),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
