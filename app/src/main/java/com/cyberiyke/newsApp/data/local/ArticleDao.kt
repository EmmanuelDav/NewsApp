package com.cyberiyke.newsApp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: List<ArticleEntity>)

    @Query("SELECT * FROM favourite_article ORDER BY articleDateTime DESC")
    fun getAllArticles():LiveData<List<ArticleEntity>>

    @Delete
    fun deleteArticle(article: ArticleEntity)

    @Query("SELECT * FROM favourite_article WHERE isFavorite = 1") // the one value is when the isfavourite is true
    fun getFavoriteArticles():LiveData<List<ArticleEntity>>

    @Query("UPDATE favourite_article SET isFavorite = :isFavourite WHERE id = :articleId") // check if an article is already in the favorites based on its unique ID
    fun updateFavoriteStatus(articleId: Int, isFavourite:Boolean)


}
