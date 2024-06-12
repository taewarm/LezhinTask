package com.example.domain.usecase

import com.example.domain.entity.SearchImageItem
import com.example.domain.repository.MainRepository
import javax.inject.Inject

class InsertBookMarkUseCase @Inject constructor(private val repository: MainRepository) {
    operator fun invoke(searchText: String, item: SearchImageItem) =
        repository.insertBookMark(searchText, item)
}