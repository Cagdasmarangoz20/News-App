package com.cagdasmarangoz.news.fragment

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
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
import com.cagdasmarangoz.news.repository.NewsRepository
import com.cagdasmarangoz.news.repository.db.ArticleDatabase
import com.cagdasmarangoz.news.utils.shareNews
import com.cagdasmarangoz.news.viewModel.savedModel.SaveViewModelFactory
import com.cagdasmarangoz.news.viewModel.savedModel.SavedViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedFragment : Fragment(R.layout.fragment_saved) {

    lateinit var viewModel: SavedViewModel
    lateinit var newsAdapter: SavedNewsAdapter
    val TAG = "breakingNewsFragment"
    private fun setupRecyclerView() {
        newsAdapter = SavedNewsAdapter()
        rvSavedNews.apply {
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
            attachToRecyclerView(rvSavedNews)
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
            rvSavedNews.visibility = View.VISIBLE
            shimmerFrameLayout2.visibility = View.GONE
            shimmerFrameLayout2.stopShimmerAnimation()
        })
    }
}