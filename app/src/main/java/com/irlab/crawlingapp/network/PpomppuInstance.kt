package com.irlab.crawlingapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PpomppuInstance {

    companion object {
        val baseURL = "https://www.ppomppu.co.kr/"

        fun getRetroInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}