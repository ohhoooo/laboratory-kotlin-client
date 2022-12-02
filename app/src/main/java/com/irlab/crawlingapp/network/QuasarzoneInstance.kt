package com.irlab.crawlingapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuasarzoneInstance {

    companion object {
        val baseURL = "https://quasarzone.com/"

        fun getRetroInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}