package com.cagdasmarangoz.news.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cagdasmarangoz.news.data.repository.NewsRepository

class SearchViewModelFactory (private val newsRepository: NewsRepository) : ViewModelProvider.Factory{

       override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(newsRepository) as T
        }



    }