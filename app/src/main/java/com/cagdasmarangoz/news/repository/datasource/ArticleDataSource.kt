package com.cagdasmarangoz.news.repository.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.cagdasmarangoz.news.model.Article
import com.cagdasmarangoz.news.model.NewsResponse
import com.cagdasmarangoz.news.repository.service.RetrofitClient
import com.cagdasmarangoz.news.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ArticleDataSource(val scope: CoroutineScope): PageKeyedDataSource<Int, Article>() {

    //for breaking news
    val breakingNews : MutableLiveData<MutableList<Article>> = MutableLiveData()
    var breakingPageNumber =1
    var breakingNewsResponse : NewsResponse? =null

    //for searching for
    val searchNews : MutableLiveData<MutableList<Article>> = MutableLiveData()
    var searchPageNumber =1
    var searchNewsResponse : NewsResponse? =null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        scope.launch{
            try {
                val response = RetrofitClient.api.getBreakingNews("us",1, Constants.API_KEY)
                when {
                    response.isSuccessful -> {
                    response.body()?.articles?.let {
                        breakingNews.postValue(it)
                        callback.onResult(it, null, 2)
                    }
                }
            }

            }catch (exception: Exception){
                Log.e("DataSource::", exception.message.toString())
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
   try {
       scope.launch {
       val response = RetrofitClient.api.getBreakingNews("us",params.requestedLoadSize, Constants.API_KEY)
       when {
           response.isSuccessful -> {
               response.body()?.articles?.let {

                   callback.onResult(it, params.key + 1)
               }
             }
           }
       }
   }catch (exception: Exception){
       Log.e("DataSource::", exception.message.toString())
   }
    }


}