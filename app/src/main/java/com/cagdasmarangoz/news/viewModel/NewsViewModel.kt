package com.cagdasmarangoz.news.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.cagdasmarangoz.news.model.Article
import com.cagdasmarangoz.news.model.NewsResponse
import com.cagdasmarangoz.news.repository.NewsRepository
import com.cagdasmarangoz.news.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel (val newsRepository: NewsRepository): ViewModel() {
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingPageNumber = 1
    var breakingNewsResponse: NewsResponse? = null

    //for searching for
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchPageNumber = 1
    var searchNewsResponse: NewsResponse? = null

    lateinit var articles: LiveData<PagedList<Article>>

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(contryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(contryCode, breakingPageNumber)
        breakingNews.postValue(handBreakingNewsResponse(response))
    }

    private fun handBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingPageNumber++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                }
                return Resource.Success(breakingNewsResponse ?:resultResponse)

            }
        }
        return Resource.Error(response.message())
    }

    private var searchJob: Job? = null

    fun getSearchdNews(queryString: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val searchNewsResponse = newsRepository.getSearchNews(queryString,searchPageNumber)
            searchNews.postValue(handleSearchNewsResponse(searchNewsResponse))
        }
    }

    private fun handleSearchNewsResponse(respons: Response<NewsResponse>): Resource<NewsResponse>? {
        if (respons.isSuccessful){
            respons.body()?.let { resultResponse->
                searchPageNumber++
                if (searchNewsResponse==null){
                    searchNewsResponse=resultResponse
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(respons.message())
    }
    fun insertArticle(article: Article)= viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }
    fun getSavedArticles()= newsRepository.getAllArticles()

    fun getBreakingNews(): LiveData<PagedList<Article>>{
        return articles
    }
}

