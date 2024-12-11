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
class NewsRemoteMediator (
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val query: String
):RemoteMediator<Int, Article>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return  MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND ->{
                val lastitem = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastitem.pager + 1
            }


        }

        return try {
            val  response = apiService.getEveryThing(
                query = query,
                pageSize = state.config.pageSize,
                page = page
            )

            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.getArticleDao().clearNonFavoriteData()
                }else{

                    val articleEntities = response.body()?.articles?.map { article ->
                        ArticleEntity(
                            articleTitle = article.title?:"",
                            articleDescription = article.description?:"",
                            articleUrl = article.url?:"",
                            publisedAt = article.publishedAt?:"",
                            articleDateTime = article.publishedAt?:"",
                            articleUrlToImage = article.urlToImage?:"",
                            articleSource = article.source.name,
                            isFavorite = false
                        )
                    }

                    if (articleEntities != null) {
                        database.getArticleDao().insertArticle(articleEntities)
                    }
                }
            }
            MediatorResult.Success(endOfPaginationReached = response.body()?.articles!!.isEmpty())
        }catch (e: Exception){
            MediatorResult.Error(e)
        }
    }
}