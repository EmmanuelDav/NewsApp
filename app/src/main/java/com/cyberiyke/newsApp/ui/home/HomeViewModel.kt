package com.cyberiyke.newsApp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberiyke.newsApp.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ArticleRepository): ViewModel() {


    var article = repository.getCachedArticles()

    var favorite = repository.getCachedArticles()

    fun fetchArticle(country: String?, category: String, language: String,pageSize:Int, page:Int ){
        viewModelScope.launch {
            repository.getTopHeadlines(country,category,language,pageSize,page)
        }
    }

    fun toggleFavoriteStatus(articleId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(articleId, isFavorite)
        }
    }

}