package com.cagdasmarangoz.news.ui.breakinnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cagdasmarangoz.news.data.repository.NewsRepository

class NewsViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }



}