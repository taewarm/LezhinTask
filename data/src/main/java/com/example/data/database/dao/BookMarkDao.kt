package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.domain.entity.BookMarkEntity

@Dao
interface BookMarkDao {
    @Insert
    fun insert(item: BookMarkEntity)

    @Delete
    fun delete(item: BookMarkEntity)

    @Query("SELECT * FROM BookMark where search_text LIKE :searchText")
    fun getBookMarkList(searchText: String): List<BookMarkEntity>

    @Query("SELECT image_url FROM BookMark where search_text LIKE :searchText AND image_url LIKE :imageUrl")
    fun getBookMarkUrl(searchText: String, imageUrl: String): String?

    @Query("DELETE FROM BookMark where image_url LIKE :imageUrl")
    fun deleteBookMark(imageUrl: String)
}