package com.cyberiyke.newsApp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cyberiyke.newsApp.data.model.Article


@Dao
interface ArticleDao {

    // Inserts an article into the database, replacing if it already exists (based on the primary key)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: ArticleEntity)

    // Retrieves all favorite articles, ordered by ID in descending order (most recent first)
    @Query("SELECT * FROM FAVOURITE_ARTICLE ORDER BY id DESC")
    fun getAllFavoriteArticles(): List<ArticleEntity>

    // Deletes a specific article from the database
    @Delete
    fun deleteArticle(article: ArticleEntity)

    // check if an article is already in the favorites based on its unique ID
    @Query("SELECT COUNT(*) FROM FAVOURITE_ARTICLE WHERE id = :articleId")
    fun isArticleFavorited(articleId: Int): Int // Returns the count (1 if present, 0 if not)
}
