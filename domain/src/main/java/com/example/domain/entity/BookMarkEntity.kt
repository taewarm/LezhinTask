package com.example.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.entity.BookMarkEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class BookMarkEntity(
    @ColumnInfo(name = "search_text") val searchText: String,
    @ColumnInfo(name = "collection") val collection: String,
    @ColumnInfo(name = "date_time") val dateTime: String,
    @ColumnInfo(name = "display_sitename") val displaySiteName: String,
    @ColumnInfo(name = "doc_url") val docUrl: String,
    @ColumnInfo(name = "width") val width: Int,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Int = 0

    companion object {
        const val TABLE_NAME = "bookmark"
    }
}
