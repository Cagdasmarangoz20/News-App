package com.cagdasmarangoz.news.utils

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cagdasmarangoz.news.R
import com.cagdasmarangoz.news.model.Article
import java.util.*


fun shareNews(context: Context?, article: Article) {

    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, article.urlToImage)
        putExtra(Intent.EXTRA_STREAM, article.urlToImage)
        putExtra(Intent.EXTRA_TITLE, article.title)
        putExtra(Intent.ACTION_DATE_CHANGED,article.publishedAt)
        type = "image/*"
    }
    context?.startActivity(Intent.createChooser(intent, "Share News On"))
}

// load image in image view
fun getCircularDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 48f
        setTint(ContextCompat.getColor(context,R.color.bgLineColor))
        start()
    }
}

fun ImageView.loadImage(url: String, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_launcher_foreground)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    if (url != null) {
        imageView.loadImage(url!!, getCircularDrawable(imageView.context))
    }
}


fun EditText.textChangeDelayedListener(delayTime: Long = 700L, textChange: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        private var timer = Timer()
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            timer.cancel()
        }

        override fun afterTextChanged(text: Editable?) {
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    val query = text?.toString()
                    textChange.invoke(query ?: "")
                }
            }, delayTime)
        }
    })
}

fun RecyclerView.onScrollEndListener(onScrollEnd :() -> Unit){
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val mLayoutManager = recyclerView.layoutManager ?: return
            val visibleItemCount = mLayoutManager.childCount
            val totalItemCount = mLayoutManager.itemCount
            var firstVisibleItemPosition = 0

            when (mLayoutManager) {
                is StaggeredGridLayoutManager -> {
                    val firstVisibleItemPositions =
                        mLayoutManager.findFirstVisibleItemPositions(null)
                    // get maximum element within the list
                    firstVisibleItemPosition = firstVisibleItemPositions[0]
                }
                is GridLayoutManager -> {
                    firstVisibleItemPosition =
                        mLayoutManager.findFirstVisibleItemPosition()
                }
                is LinearLayoutManager -> {
                    firstVisibleItemPosition =
                        mLayoutManager.findFirstVisibleItemPosition()
                }
            }
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                onScrollEnd.invoke()
            }
        }
    })
}

