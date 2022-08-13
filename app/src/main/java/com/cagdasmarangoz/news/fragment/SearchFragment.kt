package com.cagdasmarangoz.news.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cagdasmarangoz.news.R
import com.cagdasmarangoz.news.adapters.ArticleAdapter
import com.cagdasmarangoz.news.repository.NewsRepository
import com.cagdasmarangoz.news.repository.db.ArticleDatabase
import com.cagdasmarangoz.news.utils.Constants
import com.cagdasmarangoz.news.utils.Resource
import com.cagdasmarangoz.news.utils.shareNews
import com.cagdasmarangoz.news.viewModel.NewsViewModel
import com.cagdasmarangoz.news.viewModel.NewsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: ArticleAdapter
    val TAG = "breakingNewsFragment"

   /* override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.L)
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val newsRepository = NewsRepository(ArticleDatabase(it))
            val viewModelProvider = NewsViewModelFactory(newsRepository)
            viewModel = ViewModelProvider(this, viewModelProvider)[NewsViewModel::class.java]


        }
        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )

        }
        newsAdapter.onSaveClickListener {
            viewModel.insertArticle(it)
            Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show()
        }

        newsAdapter.onDeleteClickListener {

            viewModel.insertArticle(it)
            Snackbar.make(requireView(), "Removed", Snackbar.LENGTH_SHORT).show()
        }
        newsAdapter.onShareClickListener {
            shareNews(context, it)
        }
        var searchJob: Job? = null
        etSearch.addTextChangedListener { editTable ->
            searchJob?.cancel()
            searchJob = MainScope().launch {
                delay(Constants.SEARCH_TIME_DELAY)
                editTable?.let {
                    if (editTable.toString().isNotBlank()) {
                        viewModel.getSearchdNews(editTable.toString())
                    }
                }
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { newsResponse ->
            when (newsResponse) {
                is Resource.Success -> {
                    shimmerFrameLayout3.stopShimmerAnimation()
                    shimmerFrameLayout3.visibility = View.GONE
                    newsResponse.data?.let { news ->
                        newsAdapter.differ.submitList(news.articles)
                    }
                }
                is Resource.Error -> {
                    shimmerFrameLayout3.stopShimmerAnimation()
                    shimmerFrameLayout3.visibility = View.GONE
                    newsResponse.message?.let { message ->
                        Log.e(TAG, "Error :: $message")
                    }
                }
                is Resource.Loading -> {
                    shimmerFrameLayout3.startShimmerAnimation()
                    shimmerFrameLayout3.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupRecyclerView() {
        newsAdapter = ArticleAdapter()
        rvSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}