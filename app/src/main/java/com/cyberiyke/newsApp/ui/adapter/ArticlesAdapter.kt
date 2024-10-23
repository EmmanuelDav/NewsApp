package com.cyberiyke.newsApp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cyberiyke.newsApp.R
import com.cyberiyke.newsApp.data.model.Article
import com.cyberiyke.newsApp.databinding.LayoutItemNewsBinding

/**
 * Created by Emmanuel Iyke  on 3/7/2024.
 */
class ArticlesAdapter(private val listener: ((Article) -> Unit)? = null) : RecyclerView.Adapter<ArticlesAdapter.HomeViewHolder>() {

    var moviesList = mutableListOf<Article>()
        set(value) {
            field.clear()
            moviesList.addAll(value)
            notifyDataSetChanged()
        }

    fun filterList(preachingList : MutableList<Article>){
        moviesList = preachingList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount() = moviesList.count()

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    inner class HomeViewHolder(private val binding: LayoutItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) = with(itemView) {
            binding.articleTitle.text = article.title
            binding.articleDescription.text = article.description
            binding.articleDateTime.text = article.publishedAt
            binding.articleSource.text = article.source.name
            Glide.with(this)
                .load(article.urlToImage)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(binding.articleImage)
            setOnClickListener {
                listener?.invoke(moviesList[layoutPosition])
            }
        }
    }
}


