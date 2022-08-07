package com.cagdasmarangoz.news.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id : Int? =null,
    @SerializedName("author")
    var author:String?,
    @SerializedName("content")
    var content : String?,
    @SerializedName("description")
    var description : String?,
    @SerializedName("publishedAt")
    var publishedAt : String?,
    @SerializedName("source")
    var source : Source?,
    @SerializedName("title")
    var title : String?,
    @SerializedName("url")
    var url : String?,
    @SerializedName("urlToImage")
    var urlToImage : String?
) : Serializable

