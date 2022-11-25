package com.irlab.testappkotlin.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PpomppuService {

    @GET("zboard/zboard.php")
    suspend fun getDataFromAPI(
        @Query("id") id: String,
        @Query("page") page: Int,
        @Query("divpage") divpage: Int,
    ): Response<ResponseBody>
}