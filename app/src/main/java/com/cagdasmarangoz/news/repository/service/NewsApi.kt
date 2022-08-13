package com.cagdasmarangoz.news.repository.service

import com.cagdasmarangoz.news.model.NewsResponse
import com.cagdasmarangoz.news.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country :String = "TR", /*you can add your country like,us,ca etc*/
        @Query("page") pageNumber :Int ,
        @Query("apiKey") apiKey :String = Constants.API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getSearchNews(
        @Query("q") searchQuery :String,
        @Query("page") pageNumber :Int ,
        @Query("apiKey") apiKey :String = Constants.API_KEY
    ) : Response<NewsResponse>
}