package com.example.jetnews.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.jetnews.domain.entity.BookEntity
import com.example.jetnews.domain.usecase.GetBookSearchPagingDataUseCase
import com.example.jetnews.domain.usecase.UpdateBookLikeUseCase
import com.example.jetnews.presentation.model.BookModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val getBookSearchUseCase: GetBookSearchPagingDataUseCase,
    private val updateBookLikeUseCase: UpdateBookLikeUseCase,
) : ViewModel() {

    private val _queryFlow = MutableStateFlow("Kotlin")
    private val _delayedQueryFlow = _queryFlow
        .filter { it.isNotEmpty() }
        .debounce(1000L)

    private val _pagingDataFlow = _delayedQueryFlow
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
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SearchBookViewState.EMPTY)

    fun search(searchStr: String) {
        _queryFlow.update { searchStr }
    }

    fun toggleLike(bookModel: BookModel) = viewModelScope.launch {
        updateBookLikeUseCase(
            bookModel.id,
            !bookModel.isFavorite
        )
    }

}

data class SearchBookViewState(
    val query: String,
    val pagingData: Flow<PagingData<BookModel>>,
    val isLoading: Boolean
) {
    companion object {
        val EMPTY = SearchBookViewState(
            query = "",
            pagingData = flowOf(PagingData.empty()),
            isLoading = false
        )
    }
}

private fun BookEntity.map() = BookModel(
    id = isbn,
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