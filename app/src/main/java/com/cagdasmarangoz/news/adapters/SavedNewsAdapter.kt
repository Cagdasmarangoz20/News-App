package com.cagdasmarangoz.news.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cagdasmarangoz.news.R
import com.cagdasmarangoz.news.databinding.ItemArticleBinding
import com.cagdasmarangoz.news.model.Article

class SavedNewsAdapter : RecyclerView.Adapter<SavedNewsAdapter.SavedNewsViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }
    }


    inner class SavedNewsViewHolder(var view: ItemArticleBinding) :
        RecyclerView.ViewHolder(view.root)

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedNewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemArticleBinding>(
            inflater,
            R.layout.item_saved,
            parent,
            false
        )
        return SavedNewsViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: SavedNewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.view.article = article

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                article.let { article ->
                    it(article)
                }
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                article.let { article ->
                    it(article)
                }
            }
        }
        holder.view.ivShare.setOnClickListener {
            onShareNewsClick?.let {
                article.let { it1 ->
                    it(it1)
                }
            }
        }
    }







    private var onItemClickListener: ((Article) -> Unit)? = null
    private var onShareNewsClick: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }



    fun onShareClickListener(listener: ((Article) -> Unit)) {
        onShareNewsClick = listener
    }

    override fun getItemCount() = differ.currentList.size




}