package com.cyberiyke.newsApp.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.cyberiyke.newsApp.local.AppDatabase
import com.cyberiyke.newsApp.local.ArticleEntity
import com.cyberiyke.newsApp.model.Article
import com.cyberiyke.newsApp.network.ApiService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val query: String
) : RemoteMediator<Int, ArticleEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.pager + 1
            }
        }

        return try {
            val response = apiService.getEveryThing(
                query = query,
                pageSize = state.config.pageSize,
                page = page
            )

            val articles = response.body()?.articles ?: emptyList()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // Clear all non-favorite articles before inserting new ones
                    database.getArticleDao().clearNonFavoriteData()
                }

                // Map API response to database entities
                val articleEntities = articles.map { article ->
                    ArticleEntity(
                        id = article.url.hashCode(), // Generate unique ID based on URL
                        articleTitle = article.title ?: "",
                        articleDescription = article.description ?: "",
                        articleUrl = article.url ?: "",
                        articleDateTime = article.publishedAt ?: "",
                        articleUrlToImage = article.urlToImage ?: "",
                        articleSource = article.source.name,
                        isFavorite = false,
                        pager = page // Add page for pagination
                    )
                }

                database.getArticleDao().insertArticle(articleEntities)
            }

            MediatorResult.Success(endOfPaginationReached = articles.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
