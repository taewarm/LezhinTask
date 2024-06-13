package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.entity.BookMarkEntity
import com.example.domain.entity.SearchImageItem
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getSearchImage(searchText: String): Flow<PagingData<SearchImageItem>>
    fun insertBookMark(searchText: String, item: SearchImageItem): Flow<Unit>
    fun getBookMarkList(searchText: String): Flow<PagingData<BookMarkEntity>>
    fun deleteBookMark(searchText: String, imageUrl: List<String>): Flow<Unit>
}