package com.example.domain.usecase

import com.example.domain.repository.MainRepository
import javax.inject.Inject

class DeleteBookMarkUseCase @Inject constructor(private val repository: MainRepository) {
    operator fun invoke(searchText: String, imageUrl: List<String>) =
        repository.deleteBookMark(searchText, imageUrl)
}