package com.cyberiyke.newsApp.ui.home

import androidx.lifecycle.LiveData
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
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ArticleRepository): ViewModel() {


    var article = repository.getCachedArticles()

    private val _searchResults = MutableLiveData<List<ArticleEntity>>() // search results
    val searchResults: LiveData<List<ArticleEntity>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()     // Loading state to track API call progress
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()     // Error handling state
    val errorMessage: LiveData<String?>  = _errorMessage



    fun fetchArticle(country: String?, category: String, language: String, pageSize: Int?, page: Int?){
        viewModelScope.launch {
            repository.getTopHeadlines(country,category,language,pageSize!!,page!!)
        }
    }

    fun updateToggle(articleId:Int, isFavourite:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavoriteStatus(articleId,isFavourite)
        }
    }

    // this function conducts the seatch based on users input
    fun searchArticles(
        query: String,
        sources: String? = null,
        from: String? = null,
        language: String = "en",
        sortBy: String = "publishedAt",
        pageSize: Int = 20,
        page: Int = 1
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = repository.searchArticles(query, sources, from, language, sortBy, pageSize, page)
                _searchResults.value = results
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch articles: ${e.message}"
                Timber.e(e, "Error during article search")
            } finally {
                _isLoading.value = false
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

