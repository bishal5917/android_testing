package com.example.android_testing.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import com.example.android_testing.MainCoroutineRule
import com.example.android_testing.getOrAwaitValueTest
import com.example.android_testing.repositories.FakeMainRepository
import com.example.android_testing.utils.Constants
import com.example.android_testing.utils.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    //This makes sure that everything runs in order in a single thread one after another
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(FakeMainRepository())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `insert shopping item with empty field returns error`(){
        viewModel.insertShoppingItem("name","","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name returns error`(){
        val str = buildString {
           for (i in 1..Constants.MAX_NAME_LENGTH+1){
               append(i)
           }
        }
        viewModel.insertShoppingItem(str,"1","30")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price returns error`(){
        val str = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH+1){
                append(i)
            }
        }
        viewModel.insertShoppingItem("Bsal","5",str)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount returns error`(){
        viewModel.insertShoppingItem("Bsal","9999999999999999999","20.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input returns success`(){
        viewModel.insertShoppingItem("Bsal","10","20.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}