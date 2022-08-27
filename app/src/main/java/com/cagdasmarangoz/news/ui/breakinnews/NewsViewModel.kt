package com.cagdasmarangoz.news.ui.breakinnews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.cagdasmarangoz.news.model.Article
import com.cagdasmarangoz.news.model.NewsResponse
import com.cagdasmarangoz.news.data.repository.NewsRepository
import com.cagdasmarangoz.news.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel (val newsRepository: NewsRepository): ViewModel() {
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingPageNumber = 1
    val isLoading = MutableLiveData(false)
    var isPagingEnd : Boolean = false



    lateinit var articles: LiveData<PagedList<Article>>

    init {
        getBreakingNews("tr")
    }

    private fun getBreakingNews(contryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(contryCode, breakingPageNumber)
        breakingNews.postValue(handBreakingNewsResponse(response))
    }

    private fun handBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                isPagingEnd = resultResponse.articles.size < 20
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun insertArticle(article: Article)= viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getNextPageData() {
        breakingPageNumber++
        getBreakingNews("tr")
    }

}

