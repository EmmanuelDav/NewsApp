package com.cyberiyke.newsApp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cyberiyke.newsApp.local.ArticleEntity
import com.cyberiyke.newsApp.model.Article
import com.cyberiyke.newsApp.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ArticleRepository): ViewModel() {

    private val searchQuery = MutableStateFlow("Binance") // Default search query



    val article: Flow<PagingData<ArticleEntity>> = searchQuery
        .flatMapLatest { query ->
            repository.getArticles(query)
        }
        .cachedIn(viewModelScope)

    private val _searchResults = MutableLiveData<List<ArticleEntity>>() // search results
    val searchResults: LiveData<List<ArticleEntity>> get() = _searchResults


    fun setQuery(query: String) {
        searchQuery.value = query
    }



    fun updateToggle(articleId:Int, isFavourite:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavoriteStatus(articleId,isFavourite)
        }
    }

    // this function conducts the seatch based on users input
    fun searchArticles(
        query: String,
        pageSize: Int = 20,
        page: Int = 1
    ) {
        viewModelScope.launch {
            try {
                val results = repository.searchArticles(query, pageSize, page)
                _searchResults.value = results
            } catch (e: Exception) {
                Timber.e(e, "Error during article search")
            } finally {
            }
        }
    }

    fun saveArticleFromSearch(isFavourite: Boolean, article: ArticleEntity){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertSingle(article)
            repository.updateFavoriteStatus(article.id, isFavourite)
        }
    }
}

