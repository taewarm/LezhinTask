package com.example.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.database.AppDatabase
import com.example.data.network.KakaoService
import com.example.domain.entity.SearchImageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchPagingSource(
    private val kakaoService: KakaoService, private val searchText: String,
    private val database: AppDatabase
) : PagingSource<Int, SearchImageItem>() {
    override fun getRefreshKey(state: PagingState<Int, SearchImageItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchImageItem> {
        val page = params.key ?: 1
        return try {
            val response = kakaoService.getSearchImage(searchText, page)
            return LoadResult.Page(
                data = withContext(Dispatchers.IO) {
                    response.searchImageList.onEach {
                        it.bookmark = getBookMarkUrlCheck(searchText, it.imageUrl)
                    }
                },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.meta.isEnd) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private fun getBookMarkUrlCheck(searchText: String, imageUrl: String): Boolean {
        return database.bookMarkDao().getBookMarkUrl(searchText, imageUrl)?.isNotEmpty() ?: false
    }
}