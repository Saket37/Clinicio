package com.example.clinicio.main


import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*

import com.example.clinicio.R

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`

@LargeTest
@RunWith(AndroidJUnit4::class)
class SignUpFragmentTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun signUpFragmentTest() {
        val materialButton = onView(
            allOf(withId(R.id.login_as_doctor_button), withText("Continue as Doctor"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0),
                    2),
                isDisplayed()))
        materialButton.perform(click())

        val materialTextView = onView(
            allOf(withId(R.id.registerTV), withText("Register Here"),
                childAtPosition(
                    allOf(withId(R.id.signIn_constraint),
                        childAtPosition(
                            withClassName(`is`("androidx.cardview.widget.CardView")),
                            0)),
                    5),
                isDisplayed()))
        materialTextView.perform(click())

        val textInputEditText = onView(
            allOf(withId(R.id.nameInputText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nameTextField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText.perform(replaceText("Hdknhd "), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(withId(R.id.emailInputText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailTextField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText2.perform(replaceText("ksnhnm@example.com"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(withId(R.id.emailInputText), withText("ksnhnm@example.com"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emailTextField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText3.perform(pressImeActionButton())

        val textInputEditText4 = onView(
            allOf(withId(R.id.passwordInputText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userPasswordTextField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText4.perform(replaceText("Hijr&hsh586"), closeSoftKeyboard())

        val textInputEditText5 = onView(
            allOf(withId(R.id.passwordInputText), withText("Hijr&hsh586"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userPasswordTextField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText5.perform(pressImeActionButton())

        val textInputEditText6 = onView(
            allOf(withId(R.id.confirmPasswordInputText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userConfirmPasswordTextField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText6.perform(replaceText("Hijr&hsh586"), closeSoftKeyboard())

        val textInputEditText7 = onView(
            allOf(withId(R.id.confirmPasswordInputText), withText("Hijr&hsh586"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userConfirmPasswordTextField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText7.perform(pressImeActionButton())

        val textInputEditText8 = onView(
            allOf(withId(R.id.phoneInputText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userPhoneNumberField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText8.perform(replaceText("8888899999"), closeSoftKeyboard())

        val textInputEditText9 = onView(
            allOf(withId(R.id.phoneInputText), withText("8888899999"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userPhoneNumberField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText9.perform(pressImeActionButton())

        val textInputEditText10 = onView(
            allOf(withId(R.id.addressEditText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.userAddressField),
                        0),
                    0),
                isDisplayed()))
        textInputEditText10.perform(replaceText("hdi jsin"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(withId(R.id.loginButton), withText("Sign Up"),
                childAtPosition(
                    allOf(withId(R.id.signUp_constraint),
                        childAtPosition(
                            withClassName(`is`("androidx.cardview.widget.CardView")),
                            0)),
                    6),
                isDisplayed()))
       // materialButton2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int,
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
