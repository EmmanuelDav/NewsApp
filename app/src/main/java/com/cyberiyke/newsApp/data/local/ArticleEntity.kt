package com.cyberiyke.newsApp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_article")
data class ArticleEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null, // Make ID nullable to let Room auto-generate it

    var articleUrl: String = "",
    var articleTitle: String = "",
    var articleDescription: String = "",
    var articleDateTime: String = "",
    var articleSource: String = "",
    var articleUrlToImage: String = ""


    )