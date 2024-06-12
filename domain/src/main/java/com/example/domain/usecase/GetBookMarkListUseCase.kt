package com.example.domain.usecase

import com.example.domain.repository.MainRepository
import javax.inject.Inject

class GetBookMarkListUseCase @Inject constructor(private val repository: MainRepository) {
    operator fun invoke(searchText: String) = repository.getBookMarkList(searchText)
}