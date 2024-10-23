package com.cyberiyke.newsApp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cyberiyke.newsApp.R
import com.cyberiyke.newsApp.data.local.ArticleEntity
import com.cyberiyke.newsApp.data.model.Article
import com.cyberiyke.newsApp.databinding.LayoutItemNewsBinding

/**
 * Created by Emmanuel Iyke  on 3/7/2024.
 */
class ArticlesAdapter(private val listener: ((ArticleEntity) -> Unit)? = null) : RecyclerView.Adapter<ArticlesAdapter.HomeViewHolder>() {

    private var mainArticleList = mutableListOf<ArticleEntity>()
    private var searchResultsList = mutableListOf<ArticleEntity>()
    private var isSearchMode = false


    var articles: List<ArticleEntity>
        get() = if (isSearchMode) searchResultsList else mainArticleList
        set(value) {
            mainArticleList = value.toMutableList() // Update main article list
            if (!isSearchMode) {
                notifyDataSetChanged() // Refresh only if not in search mode
            }
        }

    // Method to set search results and switch to search mode
    fun setSearchResults(results: List<ArticleEntity>) {
        searchResultsList = results.toMutableList()
        isSearchMode = true
        notifyDataSetChanged()
    }

    fun exitSearchMode() {
        isSearchMode = false
        searchResultsList.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount() = articles.count()

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    inner class HomeViewHolder(private val binding: LayoutItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleEntity) = with(itemView) {
            binding.articleTitle.text = article.articleTitle
            binding.articleDescription.text = article.articleDescription
            binding.articleDateTime.text = article.publisedAt
            binding.articleSource.text = article.articleSource
            Glide.with(this)
                .load(article.articleUrlToImage)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(binding.articleImage)
            setOnClickListener {
                listener?.invoke(articles[layoutPosition])
            }
        }
    }
}


