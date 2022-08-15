package com.example.jetnews.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.jetnews.domain.entity.BookEntity
import com.example.jetnews.domain.usecase.GetBookSearchPagingDataUseCase
import com.example.jetnews.presentation.model.BookModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class SearchViewModel(
    private val getBookSearchUseCase: GetBookSearchPagingDataUseCase,
) : ViewModel() {

    private val _queryFlow = MutableStateFlow("")

    private val _pagingDataFlow = _queryFlow
        .map { query ->
            getBookSearchUseCase(query)
                .map { paging -> paging.map { it.map() } }
                .cachedIn(viewModelScope)
        }

    val viewStateFlow = combine(
        _queryFlow,
        _pagingDataFlow
    ) { query, pagingData ->
        SearchBookViewState(
            query = query,
            pagingData = pagingData,
            isLoading = false
        )
    }

    fun search(searchStr: String) {
        _queryFlow.update { searchStr }
    }

}

data class SearchBookViewState(
    val query: String,
    val pagingData: Flow<PagingData<BookModel>>,
    val isLoading: Boolean
)

private fun BookEntity.map() = BookModel(
    id = id,
    thumbUrl = thumbUrl,
    title = title,
    content = content,
    authors = authors,
    translators = translators,
    publisher = publisher,
    price = price,
    released = released,
    isFavorite = liked
)