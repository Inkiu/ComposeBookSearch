package com.example.jetnews.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetnews.domain.DomainServiceLocator
import com.example.jetnews.presentation.ui.search.SearchBookViewState
import com.example.jetnews.presentation.ui.search.SearchScreen
import com.example.jetnews.presentation.ui.search.SearchUiState
import com.example.jetnews.presentation.ui.search.SearchViewModel
import com.example.jetnews.ui.home.HomeViewModel
import com.example.jetnews.ui.theme.JetnewsTheme
import kotlinx.coroutines.flow.map

@Composable
fun BookSearchApp(
    locator: DomainServiceLocator
) {
    JetnewsTheme {
        val viewModel: SearchViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(locator.pagingUseCase) as T
                }
            }
        )
        val viewState by viewModel.viewStateFlow.collectAsState()

        SearchScreen(
            uiState = viewState.mapUiState(),
            modifier = Modifier,
            onSearchKeywordChanged = { viewModel.search(it) },
            onClickBook = {}
        )
    }
}

private fun SearchBookViewState.mapUiState(): SearchUiState {
    return SearchUiState.HasBooks(
        books = pagingData,
        isLoading = isLoading,
        keyword = query
    )
}