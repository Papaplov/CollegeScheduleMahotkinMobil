package com.example.collegeschedulemahotkinmobil.data.network

import com.example.collegeschedulemahotkinmobil.data.api.ScheduleApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5016/")  // порт должен совпадать с сервером
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ScheduleApi = retrofit.create(ScheduleApi::class.java)
}