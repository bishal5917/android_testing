package com.example.android_testing.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android_testing.getOrAwaitValue
import com.example.android_testing.launchFragmentInHiltContainer
import com.example.android_testing.ui.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    //Explicitly tell junit that to execute code one after another
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase


    private lateinit var dao: ShoppingDao

    @Before
    fun setup(){
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            ShoppingItemDatabase::class.java
//        ).allowMainThreadQueries().build()
        hiltRule.inject()
        dao = database.shoppingDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentInHiltContainer<ShoppingFragment> {

        }
    }

    @Test
    fun testInsert() = runBlocking {
        //Insert and check if it was inserted
        val item = ShoppingItem("name",1,1f,"image",1)
        dao.insertShoppingItem(item);
        //Now we got all the items
        val allItems = dao.getShoppingItems().getOrAwaitValue()
        //And now we assert
        assertThat(allItems).contains(item)
    }

    @Test
    fun testDelete() = runBlocking {
        val item = ShoppingItem("name",1,1f,"image",1)
        dao.insertShoppingItem(item);
        dao.deleteShoppingItem(item);
        val allItems = dao.getShoppingItems().getOrAwaitValue()
        assertThat(allItems).doesNotContain(item)
    }
}