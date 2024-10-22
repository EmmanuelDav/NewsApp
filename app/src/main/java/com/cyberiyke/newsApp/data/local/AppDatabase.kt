package com.cyberiyke.newsApp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database( entities = [ArticleEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao
}