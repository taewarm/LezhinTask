package com.example.data.network

import com.example.data.BuildConfig
import com.example.domain.entity.SearchImage
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoService {
    @Headers("Authorization:${BuildConfig.KAKAO_AUTHORIZATION}")
    @GET("search/image")
    suspend fun getSearchImage(
        @Query("query") query: String,
        @Query("page") page: Int
    ): SearchImage
}