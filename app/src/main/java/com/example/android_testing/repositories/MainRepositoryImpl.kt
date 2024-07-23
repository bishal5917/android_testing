package com.example.android_testing.repositories

import androidx.lifecycle.LiveData
import com.example.android_testing.data.local.ShoppingDao
import com.example.android_testing.data.local.ShoppingItem
import com.example.android_testing.data.remote.PixabayApi
import com.example.android_testing.data.remote.response.ImageResponse
import com.example.android_testing.utils.Resource
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dao: ShoppingDao,
    private val pixabayApi: PixabayApi
) : MainRepository{

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        dao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        dao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItem(): LiveData<List<ShoppingItem>> {
        return dao.getShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return dao.getTotalPrice()
    }

    override suspend fun searchForImage(image: String): Resource<ImageResponse> {
        return try {
            val res = pixabayApi.searchForImage(image)
            if (res.isSuccessful){
                res.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Unknown error",null)
            }else{
                Resource.error("Unknown error",null)
            }
        }catch (ex: Exception){
            Resource.error("Couldn't reach server",null)
        }
    }
}