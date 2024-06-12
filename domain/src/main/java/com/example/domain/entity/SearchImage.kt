package com.example.domain.entity

import com.google.gson.annotations.SerializedName

data class SearchImage(
    @SerializedName("documents") val searchImageList: List<SearchImageItem>,
    @SerializedName("meta") val meta: Meta
)