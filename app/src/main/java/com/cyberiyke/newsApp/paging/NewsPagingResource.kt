package com.cyberiyke.newsApp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cyberiyke.newsApp.model.Article
import com.cyberiyke.newsApp.network.ApiService
import javax.inject.Inject

class NewsPagingResource @Inject constructor(private val apiService: ApiService,
                                             private val query: String):
    PagingSource<Int, Article>() {


    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return  try {

            val page = params.key?: 1

            val response = apiService.getEveryThing(
                query = query,
                pageSize = params.loadSize,
                page = page
            )

            if (response.isSuccessful){
                val  article = response.body()?.articles?: emptyList()
                LoadResult.Page(
                    data = article,
                    prevKey = if(page == 1) null else page - 1,
                    nextKey = if (article.isEmpty()) null else page+ 1
                )
            } else{
                LoadResult.Error(Exception("Error because: ${response.message()}"))
            }

        }catch (e: Exception){
            LoadResult.Error(e)
        }


    }


}