package com.example.mangletext.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.unsplash.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface PhotoApiService {
    @GET("photos/random")
    suspend fun getPhoto(@Query("client_id") api_key: String,
        @Query("collections") collectionId: String): SplashPhoto
}

object PhotoApi{
    val retrofitService:PhotoApiService by lazy {retrofit.create(PhotoApiService::class.java)}
}