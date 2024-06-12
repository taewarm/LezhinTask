package com.example.domain.entity

import com.google.gson.annotations.SerializedName

data class SearchImageItem(
    @SerializedName("collection") val collection: String,
    @SerializedName("datetime") val dateTime: String,
    @SerializedName("display_sitename") val displaySiteName: String,
    @SerializedName("doc_url") val docUrl: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    var bookmark: Boolean = false
)
