package com.example.android_testing.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.android_testing.R
import com.example.android_testing.adapters.ImageAdapter
import com.example.android_testing.data.local.ShoppingDao
import com.example.android_testing.utils.Constants.BASE_URL
import com.example.android_testing.utils.Constants.DATABASE_NAME
import com.example.android_testing.data.local.ShoppingItemDatabase
import com.example.android_testing.data.remote.PixabayApi
import com.example.android_testing.repositories.MainRepository
import com.example.android_testing.repositories.MainRepositoryImpl
import com.example.android_testing.ui.ImagePickFragment
import com.example.android_testing.ui.MainViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    ) = Room.databaseBuilder(context,ShoppingItemDatabase::class.java,DATABASE_NAME).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun providesDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providesPixabayApi():PixabayApi{
        // Create an instance of HttpLoggingInterceptor
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY) // Set desired log level

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

    //Glide
    @Singleton
    @Provides
    fun providesGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background)
    )

    @Singleton
    @Provides
    fun providesImageAdaptor(
        glide : RequestManager
    ) = ImageAdapter(glide)

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder().create()
//    @Singleton
//    @Provides
//    fun providesImagePickFragment(
//        adaptor : ImageAdapter
//    ) = ImagePickFragment(imageAdapter = adaptor)
}