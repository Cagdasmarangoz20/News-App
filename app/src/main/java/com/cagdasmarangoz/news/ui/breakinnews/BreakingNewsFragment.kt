package com.cagdasmarangoz.news.ui.breakinnews

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cagdasmarangoz.news.R
import com.cagdasmarangoz.news.ui.common.adapter.ArticleAdapter
import com.cagdasmarangoz.news.databinding.FragmentBreakingNewsBinding
import com.cagdasmarangoz.news.data.repository.NewsRepository
import com.cagdasmarangoz.news.data.local.ArticleDatabase
import com.cagdasmarangoz.news.utils.Resource
import com.cagdasmarangoz.news.utils.onScrollEndListener
import com.cagdasmarangoz.news.utils.shareNews
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class BreakingNewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: ArticleAdapter
    val TAG = "breakingNewsFragment"

    private lateinit var binding: FragmentBreakingNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_breaking_news,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = ArticleAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter

            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            layoutManager = if (height>width){
                LinearLayoutManager(activity)
            }else{
                GridLayoutManager(activity,2)
            }

        }
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
            if (it.id == null) {
                it.id = Random.nextInt(0, 1000)
            }
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
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            val newsRepository = NewsRepository(ArticleDatabase(it))
            val viewModelProvider = NewsViewModelFactory(newsRepository)
            viewModel = ViewModelProvider(this,viewModelProvider)[NewsViewModel::class.java]
            binding.viewModel = viewModel
        }
        setupRecyclerView()
        setViewModelObserver()
        val mLayoutManager = LinearLayoutManager(activity)
        binding.rvBreakingNews.layoutManager = mLayoutManager

        binding.rvBreakingNews.onScrollEndListener {
            if (viewModel.isLoading.value == false && viewModel.isPagingEnd.not()) viewModel.getNextPageData()
        }
    }

    private fun setViewModelObserver() {
        viewModel.breakingNews.observe(viewLifecycleOwner) { newsResponse ->
            when (newsResponse) {
                is Resource.Success -> {
                    viewModel.isLoading.value = false
                    //binding.shimmerFrameLayout.stopShimmerAnimation()
                    //binding. shimmerFrameLayout.visibility = View.GONE
                    newsResponse.data?.let { news ->
                        binding.rvBreakingNews.visibility = View.VISIBLE
                        newsAdapter.addList(news.articles)
                    }
                }
                is Resource.Error -> {
                    viewModel.isLoading.value = false
                    //binding.shimmerFrameLayout.visibility = View.GONE
                    newsResponse.message?.let { message ->
                        Log.e(TAG, "Error :: $message")
                    }
                }
                is Resource.Loading -> {
                    viewModel.isLoading.value = true
                    binding.shimmerFrameLayout.startShimmerAnimation()
                }
            }
        }
    }
}



