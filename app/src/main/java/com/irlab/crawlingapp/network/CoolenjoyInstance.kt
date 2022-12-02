package com.irlab.crawlingapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoolenjoyInstance {

    companion object {
        val baseURL = "https://coolenjoy.net/"

        fun getRetroInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}