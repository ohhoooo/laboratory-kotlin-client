package com.irlab.crawlingapp.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CoolenjoyService {

    @GET("bbs/jirum/p{page}")
    suspend fun getDataFromAPI(@Path("page") p: Int): Response<ResponseBody>

}