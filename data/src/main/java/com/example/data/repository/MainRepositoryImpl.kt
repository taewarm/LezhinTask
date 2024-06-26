package com.example.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.database.AppDatabase
import com.example.data.network.KakaoService
import com.example.data.source.BookMarkPagingSource
import com.example.data.source.SearchPagingSource
import com.example.domain.entity.BookMarkEntity
import com.example.domain.entity.SearchImageItem
import com.example.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val context: Context,
    private val service: KakaoService,
    private val database: AppDatabase
) : MainRepository {

    override suspend fun getSearchImage(searchText: String): Flow<PagingData<SearchImageItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                SearchPagingSource(service, searchText, database)
            }
        ).flow
    }


    override fun insertBookMark(searchText: String, item: SearchImageItem): Flow<Unit> {
        return flow {
            val bookmarkItem = BookMarkEntity(
                searchText = searchText,
                collection = item.collection,
                dateTime = item.dateTime,
                displaySiteName = item.displaySiteName,
                docUrl = item.docUrl,
                width = item.width,
                height = item.height,
                imageUrl = item.imageUrl,
                thumbnailUrl = item.thumbnailUrl
            )
            database.bookMarkDao().insert(bookmarkItem)
        }
    }

    override fun deleteBookMark(
        searchText: String,
        imageUrl: List<String>
    ): Flow<Unit> {
        return flow {
            imageUrl.forEach {
                database.bookMarkDao().deleteBookMark(it)
            }
        }
    }

    override fun getBookMarkList(searchText: String): Flow<PagingData<BookMarkEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                BookMarkPagingSource("%$searchText%", database)
            }
        ).flow
    }

    companion object {
        const val TAG = "MainRepositoryImpl"
    }
}