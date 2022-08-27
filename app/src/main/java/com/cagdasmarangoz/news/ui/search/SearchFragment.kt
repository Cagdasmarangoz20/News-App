package com.cagdasmarangoz.news.ui.search

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cagdasmarangoz.news.R
import com.cagdasmarangoz.news.ui.common.adapter.ArticleAdapter
import com.cagdasmarangoz.news.databinding.FragmentSearchBinding
import com.cagdasmarangoz.news.data.repository.NewsRepository
import com.cagdasmarangoz.news.data.local.ArticleDatabase
import com.cagdasmarangoz.news.utils.Resource
import com.cagdasmarangoz.news.utils.shareNews
import com.cagdasmarangoz.news.utils.textChangeDelayedListener
import com.google.android.material.snackbar.Snackbar



class SearchFragment : Fragment() {

    lateinit var viewModel: SearchViewModel
    lateinit var newsAdapter: ArticleAdapter
    val TAG = "breakingNewsFragment"


    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val newsRepository = NewsRepository(ArticleDatabase(it))
            val viewModelProvider = SearchViewModelFactory(newsRepository)
            viewModel = ViewModelProvider(this, viewModelProvider)[SearchViewModel::class.java]
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
        binding.etSearch.textChangeDelayedListener { query ->
            if (query.isNotBlank()) {
                viewModel.getSearchdNews(query)
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { newsResponse ->
            when (newsResponse) {
                is Resource.Success -> {
                    binding.shimmerFrameLayout3.stopShimmerAnimation()
                    binding.shimmerFrameLayout3.visibility = View.GONE
                    newsResponse.data?.let { news ->
                        newsAdapter.submitList(news.articles)
                    }
                }
                is Resource.Error -> {
                    binding.shimmerFrameLayout3.stopShimmerAnimation()
                    binding.shimmerFrameLayout3.visibility = View.GONE
                    newsResponse.message?.let { message ->
                        Log.e(TAG, "Error :: $message")
                    }
                }
                is Resource.Loading -> {
                    binding.shimmerFrameLayout3.startShimmerAnimation()
                    binding.shimmerFrameLayout3.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupRecyclerView() {
        newsAdapter = ArticleAdapter()
        binding.rvSearch.apply {
            adapter = newsAdapter
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            layoutManager = if (height > width) {
                LinearLayoutManager(activity)
            } else {
                GridLayoutManager(activity, 2)
            }
        }
    }

}