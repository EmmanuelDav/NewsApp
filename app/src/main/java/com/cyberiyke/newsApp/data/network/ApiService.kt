package com.cyberiyke.newsApp.data.network

import androidx.room.Query
import com.cyberiyke.newsApp.data.model.NewsResponse
import retrofit2.http.GET
import java.util.*

interface ApiService {

        @GET("/v2/top-headlines")
        suspend fun top(
            @Query("country") country:String? = null,
            @Query("category") category:String? = null,
            @Query("language") language:String = "en",
            @Query("sources") sources: String? = null,
            @Query("q") keyword: String? = null,
            @Query("pageSize") pageSize: Int = 20, // Added default value
            @Query("page") page: Int = 1 // Added default value

        ): NewsResponse


        @GET("/v2/everything")
        suspend fun everyThing(
            @Query("q") query: String,
            @Query("sources") sources: String? = null,
            @Query("from") from:String? = null,
            @Query("language") language: String = "en",
            @Query("sortBy") sortBy:String = "publishedAt",
            @Query("pageSize") pageSize: Int = 20, // Added default value
            @Query("page") page: Int = 1 // Added default value
        ):NewsResponse

}