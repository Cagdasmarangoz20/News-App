package com.cagdasmarangoz.news.repository

import com.cagdasmarangoz.news.model.Article
import com.cagdasmarangoz.news.repository.db.ArticleDatabase
import com.cagdasmarangoz.news.repository.service.RetrofitClient

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode : String,pageNumber:Int)=
        RetrofitClient.api.getBreakingNews(countryCode,pageNumber)

    suspend fun getSearchNews(q:String,pageNumber: Int)=
        RetrofitClient.api.getSearchNews(q,pageNumber)

    suspend fun upsert(article: Article)= db.getArticleDao().insert(article)
    suspend fun delete(article: Article)= db.getArticleDao().deleteArticle(article)

    fun getAllArticles()= db.getArticleDao().getArticles()
}