package com.cyberiyke.newsApp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database( entities = [ArticleEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        const val DATABASE_NAME = "news_app_db" // Centralized database name
    }
}