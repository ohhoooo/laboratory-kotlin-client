package com.irlab.crawlingapp.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuasarzoneService {

    @GET("bbs/qb_saleinfo")
    suspend fun getDataFromAPI(@Query("page") query: Int): Response<ResponseBody>
}