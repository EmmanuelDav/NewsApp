package com.cyberiyke.newsApp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.cyberiyke.newsApp.local.AppDatabase
import com.cyberiyke.newsApp.local.ArticleDao
import com.cyberiyke.newsApp.local.ArticleEntity
import com.cyberiyke.newsApp.model.Article
import com.cyberiyke.newsApp.network.ApiService
import com.cyberiyke.newsApp.network.NetworkResult
import com.cyberiyke.newsApp.paging.NewsRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject


@OptIn(androidx.paging.ExperimentalPagingApi::class)
class ArticleRepository @Inject constructor(
    private val apiService: ApiService,
    private val articleDao: ArticleDao,
    private val database: AppDatabase,
    private val remoteMediator: NewsRemoteMediator
) {

    var networkResult: StateFlow<NetworkResult> = remoteMediator.networkResult

    // here we are fetching articles from the api and caching them in room database
    fun getArticles(query: String): Flow<PagingData<ArticleEntity>> {

        remoteMediator.query = query // Dynamically set the query

        val pagingSourceFactory = { database.getArticleDao().getAllArticles() }



        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),

            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    suspend fun searchArticles(
        query: String,
        pageSize: Int = 20,
        page: Int = 1
    ): List<ArticleEntity> {
        // Fetch from the API
        val response = apiService.getEveryThing(query, pageSize, page)

        return try {
            if (response.isSuccessful) {
                response.body()?.articles?.map { article ->
                    ArticleEntity(
                        articleTitle = article.title ?: "",
                        articleDescription = article.description ?: "",
                        articleUrl = article.url ?: "",
                        publisedAt = article.publishedAt ?: "",
                        articleDateTime = article.publishedAt ?: "",
                        articleUrlToImage = article.urlToImage ?: "",
                        articleSource = article.source.name,
                        isFavorite = false,
                        pager = 0
                    )
                } ?: emptyList()
            } else {
                Timber.e("Error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Timber.e(e, "Error occurred while searching articles")
            emptyList()
        }
    }


    suspend fun updateFavoriteStatus(articleId: Int, isFavourite: Boolean) {
        articleDao.updateFavoriteStatus(articleId, isFavourite)
    }

    fun getFavouriteArticle(): LiveData<List<ArticleEntity>> {
        return articleDao.getFavoriteArticles()
    }

    suspend fun insertSingle(articleEntity: ArticleEntity) {
        articleDao.insertArticle(listOf(articleEntity))
    }

}