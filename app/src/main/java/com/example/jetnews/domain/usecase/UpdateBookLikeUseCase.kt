package com.example.jetnews.domain.usecase

import com.example.jetnews.domain.repository.BookRepository

class UpdateBookLikeUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        id: Long,
        like: Boolean
    ) = repository.updateBookLike(id, like)
}