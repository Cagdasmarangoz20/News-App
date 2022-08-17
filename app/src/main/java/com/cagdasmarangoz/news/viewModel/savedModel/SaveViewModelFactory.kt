package com.cagdasmarangoz.news.viewModel.savedModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cagdasmarangoz.news.repository.NewsRepository

class SaveViewModelFactory (private val newsRepository: NewsRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SavedViewModel(newsRepository) as T
        }



    }