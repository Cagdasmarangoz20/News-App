package com.cagdasmarangoz.news.viewModel.savedModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.cagdasmarangoz.news.model.Article
import com.cagdasmarangoz.news.model.NewsResponse
import com.cagdasmarangoz.news.repository.NewsRepository
import com.cagdasmarangoz.news.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class SavedViewModel(val newsRepository: NewsRepository): ViewModel() {

        val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var breakingPageNumber = 1
        var breakingNewsResponse: NewsResponse? = null



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
                    breakingPageNumber++
                    if (breakingNewsResponse == null) {
                        breakingNewsResponse = resultResponse
                    }
                    return Resource.Success(breakingNewsResponse ?:resultResponse)

                }
            }
            return Resource.Error(response.message())
        }





        fun insertArticle(article: Article)= viewModelScope.launch {
            newsRepository.upsert(article)
        }
        fun deleteArticle(article: Article) = viewModelScope.launch {
            newsRepository.delete(article)
        }
        fun getSavedArticles()= newsRepository.getAllArticles()

    }
