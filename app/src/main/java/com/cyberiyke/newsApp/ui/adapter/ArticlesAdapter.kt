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

    var articleMutableList = mutableListOf<ArticleEntity>()
        set(value) {
            field.clear()
            articleMutableList.addAll(value)
            notifyDataSetChanged()
        }

    fun filterList(preachingList : MutableList<ArticleEntity>){
        articleMutableList = preachingList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount() = articleMutableList.count()

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(articleMutableList[position])
    }

    inner class HomeViewHolder(private val binding: LayoutItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleEntity) = with(itemView) {
            binding.articleTitle.text = article.articleTitle
            binding.articleDescription.text = article.articleDescription
          //  binding.articleDateTime.text = article.publishedAt
            binding.articleSource.text = article.articleSource
            Glide.with(this)
                .load(article.articleUrlToImage)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(binding.articleImage)
            setOnClickListener {
                listener?.invoke(articleMutableList[layoutPosition])
            }
        }
    }
}


