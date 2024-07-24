package com.example.android_testing.di

import android.content.Context
import androidx.room.Room
import com.example.android_testing.data.local.ShoppingDao
import com.example.android_testing.utils.Constants.BASE_URL
import com.example.android_testing.utils.Constants.DATABASE_NAME
import com.example.android_testing.data.local.ShoppingItemDatabase
import com.example.android_testing.data.remote.PixabayApi
import com.example.android_testing.repositories.MainRepository
import com.example.android_testing.repositories.MainRepositoryImpl
import com.example.android_testing.ui.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesOfflineDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,ShoppingItemDatabase::class.java,DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providesDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providesPixabayApi():PixabayApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayApi::class.java)
    }

    @Singleton
    @Provides
    fun providesDefaultShoppingRepository(
        dao : ShoppingDao,
        api : PixabayApi
    ) = MainRepositoryImpl(dao,api) as MainRepository

    //viewmodel
    @Singleton
    @Provides
    fun providesMainViewModel(repo:MainRepository) = MainViewModel(repo)
}