package com.example.lezhintask.ui.bookmark

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.BookMarkEntity
import com.example.domain.usecase.DeleteBookMarkUseCase
import com.example.domain.usecase.GetBookMarkListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val _progress = MutableStateFlow(false)
    val progress = _progress.asStateFlow()
    val bookMarkListState = mutableStateListOf<BookMarkEntity>()
    fun updateBookMarkText(text: String) {
        _bookMarkText.value = text
    }

    fun showProgress(bool: Boolean) {
        _progress.value = bool
    }

    fun deleteBookMark(searchText: String, imageUrl: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteBookMarkUseCase(searchText, imageUrl).catch { e -> Log.e(TAG, e.message, e) }
                .collectLatest {
                    bookMarkListState.clear()
                    bookMarkListState.addAll(it)
                }
        }
    }

    fun getBookMarkList(searchText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getBookMarkListUseCase(searchText).catch { e -> Log.e(TAG, e.message, e) }
                .collectLatest {
                    bookMarkListState.clear()
                    bookMarkListState.addAll(it)
                    _progress.value = false
                }
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}