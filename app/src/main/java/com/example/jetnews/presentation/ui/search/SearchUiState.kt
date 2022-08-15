package com.example.jetnews.presentation.ui.search

import com.example.jetnews.presentation.model.BookModel

sealed interface SearchUiState {

    val isLoading: Boolean
    val keyword: String

    data class NoBooks(
        override val isLoading: Boolean,
        override val keyword: String
    ) : SearchUiState

    data class HasBooks(
        val books: List<BookModel>,
        override val isLoading: Boolean,
        override val keyword: String
    ) : SearchUiState

    data class Error(
        val exception: AppException,
        override val isLoading: Boolean,
        override val keyword: String
    ) : SearchUiState

}

sealed interface AppException {
    object Network : AppException
}