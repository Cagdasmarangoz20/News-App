package com.cagdasmarangoz.news.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cagdasmarangoz.news.R
import com.cagdasmarangoz.news.databinding.ItemArticleBinding
import com.cagdasmarangoz.news.model.Article

class ArticleAdapter : ListAdapter<Article,ArticleAdapter.ArticleViewHolder>(
    object  : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return  oldItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return newItem == newItem
        }
    }
){

    class ArticleViewHolder(var view : ItemArticleBinding) : RecyclerView.ViewHolder(view.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
    val inflater = LayoutInflater.from(parent.context)
      val view = DataBindingUtil.inflate<ItemArticleBinding>(inflater, R.layout.item_article,parent, false)
      return ArticleViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
       val article = getItem(position)

        holder.view.article = article

        holder.itemView.setOnClickListener{
            onItemClickListener?.let {
                it(article)
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(article)
            }
        }
        holder. view.ivShare.setOnClickListener{
            onShareNewsClick?.let {
                it(article)
            }
        }
        holder.view.ivSave.setOnClickListener{
            if (holder.view.ivSave.tag.toString().toInt()==0){
                holder.view.ivSave.tag = 1
                holder.view.ivSave.setImageDrawable(it.resources.getDrawable(R.drawable.ic_saved))
                onArticleSaveClick?.let {
                    if (article != null) {
                        it(article)
                    }
                }
            }else{
                holder.view.ivSave.tag = 0
                holder.view.ivSave.setImageDrawable(it.resources.getDrawable(R.drawable.ic_save))
                onArticleSaveClick?.let {
                    if (article != null) {
                        it(article)
                    }
                }
            }
            onArticleSaveClick?.let {
                article?.let { it1->it(it1)}
            }
        }


    }

    override fun getItemId(position: Int)=position.toLong()

    private var onItemClickListener: ((Article)-> Unit)? = null
    private var onArticleSaveClick: ((Article)-> Unit)? = null
    private var onArticleDeleteClick: ((Article)-> Unit)? = null
    private var onShareNewsClick: ((Article)-> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener=listener
    }
    fun onSaveClickListener(listener : ((Article)->Unit)){
        onArticleSaveClick=listener
    }
    fun onDeleteClickListener(listener: (Article) -> Unit){
        onArticleDeleteClick=listener
    }
    fun onShareClickListener(listener : ((Article)->Unit)){
        onShareNewsClick=listener
    }



}