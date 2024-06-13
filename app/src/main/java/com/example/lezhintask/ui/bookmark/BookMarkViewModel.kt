package com.example.lezhintask.ui.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.BookMarkEntity
import com.example.domain.usecase.DeleteBookMarkUseCase
import com.example.domain.usecase.GetBookMarkListUseCase
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
class BookMarkViewModel @Inject constructor(
    private val deleteBookMarkUseCase: DeleteBookMarkUseCase,
    private val getBookMarkListUseCase: GetBookMarkListUseCase,
) : ViewModel() {
    private val _bookMarkText = MutableStateFlow("")
    val bookMarkText = _bookMarkText.asStateFlow()
    private val _bookMarkList = MutableStateFlow<PagingData<BookMarkEntity>>(PagingData.empty())
    val bookMarkList: StateFlow<PagingData<BookMarkEntity>> = _bookMarkList
    fun updateBookMarkText(text: String) {
        _bookMarkText.value = text
    }

    fun deleteBookMark(searchText: String, imageUrl: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                deleteBookMarkUseCase(searchText, imageUrl).catch { e -> Log.e(TAG, e.message, e) }
                    .collectLatest {}
            }.join()
            getBookMarkListUseCase(searchText).cachedIn(viewModelScope)
                .catch { e -> Log.e(TAG, e.message, e) }
                .collectLatest {
                    _bookMarkList.emit(it)
                }
        }
    }

    fun getBookMarkList(searchText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getBookMarkListUseCase(searchText).cachedIn(viewModelScope)
                .catch { e -> Log.e(TAG, e.message, e) }
                .collectLatest {
                    _bookMarkList.emit(it)
                }
        }
    }

    companion object {
        const val TAG = "BookMarkViewModel"
    }
}