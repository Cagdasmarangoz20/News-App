package com.cagdasmarangoz.news.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cagdasmarangoz.news.model.Article

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Query("SELECT * FROM articles")
    fun getArticles() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}