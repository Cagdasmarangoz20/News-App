package com.cagdasmarangoz.news.ui.article

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.cagdasmarangoz.news.R
import com.cagdasmarangoz.news.data.repository.NewsRepository
import com.cagdasmarangoz.news.data.local.ArticleDatabase
import com.cagdasmarangoz.news.ui.breakinnews.NewsViewModel
import com.cagdasmarangoz.news.ui.breakinnews.NewsViewModelFactory
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            val newsRepository = NewsRepository(ArticleDatabase(it))
            val viewModelProvider = NewsViewModelFactory(newsRepository)
            viewModel = ViewModelProvider(this, viewModelProvider)[NewsViewModel::class.java]

        }
        val article = args.article
        webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)
                    progressBar.visibility = View.GONE

                }
            }
            loadUrl((article.url.toString()))
        }
    }


}