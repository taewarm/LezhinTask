package com.example.lezhintask.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.SearchImageItem
import com.example.domain.usecase.DeleteBookMarkUseCase
import com.example.domain.usecase.GetSearchImageUseCase
import com.example.domain.usecase.InsertBookMarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchImageUseCase: GetSearchImageUseCase,
    private val insertBookMarkUseCase: InsertBookMarkUseCase,
    private val deleteBookMarkUseCase: DeleteBookMarkUseCase,
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _searchList = MutableStateFlow<PagingData<SearchImageItem>>(PagingData.empty())
    val searchList: StateFlow<PagingData<SearchImageItem>> = _searchList

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun getSearchImage(searchText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getSearchImageUseCase(searchText).cachedIn(viewModelScope)
                .catch { e -> Log.e(TAG, e.message, e) }.collectLatest {
                    _searchList.emit(it)
                }
        }
    }

    fun insertBookMark(searchText: String, item: SearchImageItem) {
        viewModelScope.launch(Dispatchers.IO) {
            insertBookMarkUseCase(searchText, item).catch { e -> Log.e(TAG, e.message, e) }
                .collectLatest { Log.i(TAG, "추가 성공") }
        }
    }

    fun deleteBookMark(searchText: String, imageUrl: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteBookMarkUseCase(searchText, imageUrl).catch { e -> Log.e(TAG, e.message, e) }
                .collectLatest { Log.i(TAG, "삭제 성공") }
        }
    }

    companion object {
        const val TAG = "SearchViewModel"
    }
}