package com.example.mangletext.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Logger.Companion
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://quotes.rest"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

val client : OkHttpClient = OkHttpClient.Builder().apply {
    this.addInterceptor(interceptor)
}.build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface QuoteApiService {
    @GET("qod")
    suspend fun getQuote():Result
}

object QuoteApi{
    val retrofitService:QuoteApiService by lazy {retrofit.create(QuoteApiService::class.java)}
}
