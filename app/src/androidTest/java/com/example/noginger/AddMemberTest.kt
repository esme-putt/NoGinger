package com.example.noginger

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddMemberTest {

    @get: Rule
    var mActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun SetUp() {
        onView(withId(R.id.add_person_fab)).perform(ViewActions.click())
        onView(withId(R.id.nameTextUserInput))
            .perform(ViewActions.typeText("James"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.submitButton)).perform(ViewActions.click())
    }

    // Checks that the added user has their name registered in the recycler view
    @Test
    fun memberAddedTest() {
        onView(withId(R.id.member_view))
            .check(matches(hasDescendant(withChild(withText("James")))))
    }
}