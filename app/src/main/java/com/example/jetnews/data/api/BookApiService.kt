package com.example.jetnews.data.api

import com.soochang.data.v2.api.BookApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {

    @GET("v3/search/book")
    suspend fun getBookList(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("target") target: String,
    ) : BookApiResponse

}