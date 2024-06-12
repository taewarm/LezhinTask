package com.example.domain.usecase

import com.example.domain.repository.MainRepository
import javax.inject.Inject

class GetSearchImageUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(searchText: String) = repository.getSearchImage(searchText)
}