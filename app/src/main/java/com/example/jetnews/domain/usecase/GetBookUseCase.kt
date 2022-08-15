package com.example.jetnews.domain.usecase

import com.example.jetnews.domain.entity.BookEntity
import com.example.jetnews.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetBookUseCase(
    private val repository: BookRepository
) {
    operator fun invoke(
        id: Long
    ): Flow<BookEntity> = repository.getBook(id)
}