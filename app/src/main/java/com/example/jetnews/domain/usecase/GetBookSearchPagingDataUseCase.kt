package com.example.jetnews.domain.usecase

import androidx.paging.PagingData
import com.example.jetnews.domain.entity.BookEntity
import com.example.jetnews.domain.repository.BookRepository
import kotlinx.coroutines.flow.*

class GetBookSearchPagingDataUseCase(
    private val repository: BookRepository
) {

    operator fun invoke(query: String): Flow<PagingData<BookEntity>> =
        repository.getBookPagingDataFlow(query)

}