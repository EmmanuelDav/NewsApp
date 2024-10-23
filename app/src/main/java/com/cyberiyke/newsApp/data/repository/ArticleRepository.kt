package com.cyberiyke.newsApp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.cyberiyke.newsApp.data.local.ArticleDao
import com.cyberiyke.newsApp.data.local.ArticleEntity
import com.cyberiyke.newsApp.data.network.ApiService
import timber.log.Timber
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val apiService: ApiService,
    private val articleDao: ArticleDao
) {

     // here we are fetching articles from the api and caching them in room database
    suspend fun getTopHeadlines(
        country: String?,
        category: String?,
        language: String,
        pageSize: Int,
        page: Int
    ){

        // firstly fetch from API
        val response =  apiService.getTopHeadline(
            country,
            category,
            language,
            null,
            null,
            pageSize,
            page
        )

         // next we cache the fetched articles response into our database
         try {
             if (response.isSuccessful){
                 response.body()?.let { newsResponse ->
                     val articleEntities = newsResponse.articles.map { article ->
                         ArticleEntity(
                             id = 0,
                             articleTitle = article.title?:"",
                             articleDescription = article.description?:"",
                             articleUrl = article.url?:"",
                             articleDateTime = article.publishedAt?:"",
                             articleUrlToImage = article.urlToImage?:"",
                             articleSource = article.source.name,
                             isFavorite = false
                         )
                     }
                     Timber.d("Inserting ${articleEntities.size} articles into the database")
                     articleDao.insertArticle(articleEntities)
                 }
             }

         }catch (e: Exception){
             Timber.e(e, "Error occurred while fetching articles")
         }
    }

    // Fetch cached articles when offline
    fun getCachedArticles(): List<ArticleEntity> {
        return articleDao.getAllArticles()
    }

    fun updateFavoriteStatus(articleId: Int, isFavourite:Boolean){
      //  articleDao.updateFavoriteStatus(articleId, isFavourite)
    }

    fun getFavouriteArticle():LiveData<List<ArticleEntity>>{
        return articleDao.getFavoriteArticles()
    }

}