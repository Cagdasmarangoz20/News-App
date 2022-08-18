package com.cagdasmarangoz.news.fragment

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cagdasmarangoz.news.R
import com.cagdasmarangoz.news.adapters.SavedNewsAdapter
import com.cagdasmarangoz.news.databinding.FragmentSavedBinding
import com.cagdasmarangoz.news.repository.NewsRepository
import com.cagdasmarangoz.news.repository.db.ArticleDatabase
import com.cagdasmarangoz.news.utils.shareNews
import com.cagdasmarangoz.news.viewModel.savedModel.SaveViewModelFactory
import com.cagdasmarangoz.news.viewModel.savedModel.SavedViewModel
import com.google.android.material.snackbar.Snackbar


class SavedFragment : Fragment() {

    lateinit var viewModel: SavedViewModel
    lateinit var newsAdapter: SavedNewsAdapter
    val TAG = "breakingNewsFragment"

    private lateinit var binding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    private fun setupRecyclerView() {
        newsAdapter = SavedNewsAdapter()
        binding.rvSavedNews.apply {
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
                R.id.action_savedFragment_to_articleFragment,
                bundle
            )

        }
        newsAdapter.onShareClickListener {
            shareNews(context, it)
        }
        val onItemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.currentList[position]
                viewModel.deleteArticle(article)

                Snackbar.make(requireView(),"Delete Successfully",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.insertArticle(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(onItemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val newsRepository = NewsRepository(ArticleDatabase(it))
            val viewModelProvider = SaveViewModelFactory(newsRepository)
            viewModel = ViewModelProvider(this, viewModelProvider)[SavedViewModel::class.java]


        }
        setupRecyclerView()
        setViewModelObserver()
    }

    private fun setViewModelObserver() {
        viewModel.getSavedArticles().observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "" + it.size)
            newsAdapter.submitList(it)
            binding.rvSavedNews.visibility = View.VISIBLE
            binding.shimmerFrameLayout2.visibility = View.GONE
            binding.shimmerFrameLayout2.stopShimmerAnimation()
        })
    }
}