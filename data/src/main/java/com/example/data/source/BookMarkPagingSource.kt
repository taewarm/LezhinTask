package com.example.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.database.AppDatabase
import com.example.domain.entity.BookMarkEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookMarkPagingSource(
    private val searchText: String, private val database: AppDatabase
) : PagingSource<Int, BookMarkEntity>() {
    override fun getRefreshKey(state: PagingState<Int, BookMarkEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookMarkEntity> {
        val page = params.key ?: 0
        val limit = 20
        val offset = page * limit
        return try {
            return withContext(Dispatchers.IO) {
                val response =
                    database.bookMarkDao().getBookMarkListPaging(searchText, limit, offset)
                LoadResult.Page(
                    data = response,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (response.isEmpty()) null else page + 1
                )
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}