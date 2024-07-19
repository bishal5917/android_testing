package com.example.android_testing.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM shopping_items")
    //Note : We dont need to make this suspend fun because this returns LiveData
//    and LiveData is asynchronous by default
    fun getShoppingItems() : LiveData<List<ShoppingItem>>

    @Query("SELECT SUM(price*amount) FROM shopping_items")
    fun getTotalPrice() : LiveData<Float>
}