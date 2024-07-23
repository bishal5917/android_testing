package com.example.android_testing.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_testing.data.local.ShoppingItem
import com.example.android_testing.data.remote.response.ImageResponse
import com.example.android_testing.repositories.MainRepository
import com.example.android_testing.utils.Constants
import com.example.android_testing.utils.Event
import com.example.android_testing.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItem()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images : LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currImageUrl = MutableLiveData<String>()
    val currImageUrl : LiveData<String> = _currImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus : LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurrentImageUrl(url : String){
        _currImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem : ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertItemIntoDb(shoppingItem : ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String,amt:String,price:String){
         if (name.isEmpty() || amt.isEmpty() || price.isEmpty()){
             _insertShoppingItemStatus.postValue(
                 Event(Resource.error("Fields mustn't be empty",null))
             )
             return
         }
        if (name.length > Constants.MAX_NAME_LENGTH){
            _insertShoppingItemStatus.postValue(
                Event(Resource.error("Name is too long",null))
            )
            return
        }
        if (price.length > Constants.MAX_PRICE_LENGTH){
            _insertShoppingItemStatus.postValue(
                Event(Resource.error("Price is too high",null))
            )
            return
        }
        val amount = try {
            amt.toInt()
        } catch (e : Exception){
            _insertShoppingItemStatus.postValue(
                Event(Resource.error("Please enter a valid amount",null))
            )
            return
        }
        val item = ShoppingItem(name,amount,price.toFloat(),_currImageUrl.value ?:"")
        insertItemIntoDb(item)
        setCurrentImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(item)))
    }

    fun searchForImage(imageQuery : String){
        if (imageQuery.isEmpty()){
            return
        }
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }
}