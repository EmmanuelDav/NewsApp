package com.cyberiyke.newsApp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberiyke.newsApp.data.local.ArticleEntity
import com.cyberiyke.newsApp.data.model.Article
import com.cyberiyke.newsApp.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ArticleRepository): ViewModel() {



    fun fetchArticle(country: String?, category: String, language: String, pageSize: Int?, page: Int?){
        viewModelScope.launch {
            repository.getTopHeadlines(country,category,language,pageSize!!,page!!)
        }
    }

    suspend fun getNews(): List<ArticleEntity> {
        return withContext(Dispatchers.IO) {
            repository.getCachedArticles()
        }
    }

}