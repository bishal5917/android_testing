package com.example.android_testing.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.android_testing.R
import com.example.android_testing.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun clickAddShoopingItemBtnNavigatesToAddShoppingFragment(){
        val navcontroller = mock(NavController::class.java)
        launchFragmentInHiltContainer<ShoppingFragment> {
            Navigation.setViewNavController(requireView(),navcontroller)
        }
        onView(withId(R.id.btnAddShoppingItem )).perform(click())

        verify(navcontroller).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
        )
    }

}