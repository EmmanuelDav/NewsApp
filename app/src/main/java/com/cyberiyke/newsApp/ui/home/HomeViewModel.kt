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


    private var _articlesLiveData  = MutableLiveData<List<ArticleEntity>>(emptyList())
    val articleLiveData:MutableLiveData<List<ArticleEntity>> get() = _articlesLiveData

    private var _favoriteArticlesLiveData = MutableLiveData<List<ArticleEntity>>()
    val favoriteArticlesLiveData: MutableLiveData<List<ArticleEntity>> = _favoriteArticlesLiveData


    fun fetchArticle(country: String?, category: String, language: String, pageSize: Int?, page: Int?){
        viewModelScope.launch {
            repository.getTopHeadlines(country,category,language,pageSize!!,page!!)
        }
    }

    fun toggleFavoriteStatus(articleId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(articleId, isFavorite)
        }
    }

    fun fetchCachedArticles() {
        viewModelScope.launch {
            val articles = withContext(Dispatchers.IO) {
                repository.getCachedArticles()
            }
            // Update the LiveData or any other UI-related operation on the main thread
            _articlesLiveData.postValue(articles.value)
        }
    }

    fun fetchFavoriteArticles() {
        viewModelScope.launch {
            val favoriteArticles = withContext(Dispatchers.IO) {
                repository.getFavouriteArticle()
            }
            _favoriteArticlesLiveData.postValue(favoriteArticles.value)
        }
    }


}