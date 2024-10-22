package com.cyberiyke.newsApp.data.model

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("author")
    var author : String,

    @SerializedName("content")
    var content: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("publishedAt")
    var publishedAt: String,

    @SerializedName("source")
    var source: Source,

    @SerializedName("title")
    var title : String,

    @SerializedName("url")
    var url: String,

    @SerializedName("urlToImage")
    var urlToImage: String

)

data class Source(
    @SerializedName("id")
    var id: String = "no_id",
    @SerializedName("name")
    var name: String = "no_name"
)