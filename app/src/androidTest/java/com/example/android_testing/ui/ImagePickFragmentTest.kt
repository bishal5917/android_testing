package com.example.android_testing.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.android_testing.R
import com.example.android_testing.adapters.ImageAdapter
import com.example.android_testing.getOrAwaitValue
import com.example.android_testing.launchFragmentInHiltContainer
import com.example.android_testing.repositories.FakeMainRepositoryAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@HiltAndroidTest
@MediumTest
@ExperimentalCoroutinesApi
class ImagePickFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun afterImageClickedPopBackstackAndSetImageUrl(){
        val navcontroller = mock(NavController::class.java)
        val imgUrl = "TEST";
        val testViewModel = MainViewModel(FakeMainRepositoryAndroidTest())
        launchFragmentInHiltContainer<ImagePickFragment> (fragmentFactory=fragmentFactory){
            Navigation.setViewNavController(requireView(),navcontroller)
            imageAdapter.images = listOf(imgUrl)
            viewModel = testViewModel
        }

        onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0,
                click()
            )
        )
        verify(navcontroller).popBackStack()
        assertThat(testViewModel.currImageUrl.getOrAwaitValue()).isEqualTo(imgUrl)
    }
}