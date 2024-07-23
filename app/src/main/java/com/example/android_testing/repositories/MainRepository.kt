package com.example.android_testing.repositories

import androidx.lifecycle.LiveData
import com.example.android_testing.data.local.ShoppingItem
import com.example.android_testing.data.remote.response.ImageResponse
import com.example.android_testing.utils.Resource

interface MainRepository  {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItem() : LiveData<List<ShoppingItem>>

    fun observeTotalPrice() : LiveData<Float>

    suspend fun searchForImage(image : String) : Resource<ImageResponse>
}