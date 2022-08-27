package com.cagdasmarangoz.news.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cagdasmarangoz.news.data.repository.NewsRepository

class SaveViewModelFactory (private val newsRepository: NewsRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SavedViewModel(newsRepository) as T
        }



    }