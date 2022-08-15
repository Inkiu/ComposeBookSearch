package com.example.jetnews.presentation.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.presentation.model.BookModel
import com.example.jetnews.presentation.ui.book.BookRow
import com.example.jetnews.ui.theme.JetnewsTheme
import com.example.jetnews.presentation.ui.widget.SearchInput

@Composable
private fun SearchScreen(
    uiState: SearchUiState,
    modifier: Modifier,
    onSearchKeywordChanged: (String) -> Unit,
    onClickBook: (BookModel) -> Unit,
) {
    Column(
        modifier = modifier
    ){
        SearchInput(
            onSearchInputChanged = onSearchKeywordChanged,
            modifier = Modifier.padding(3.dp),
            keyword = uiState.keyword
        )
        Spacer(modifier = Modifier.padding(vertical = 3.dp))
        when (uiState) {
            is SearchUiState.HasBooks -> HasBooksContent(
                uiState = uiState,
                onClickBook = onClickBook
            )
            is SearchUiState.NoBooks -> NoBooksContent(
                uiState = uiState
            )
            is SearchUiState.Error -> ErrorContent(
                uiState = uiState
            )
        }
    }
}

@Composable
private fun HasBooksContent(
    uiState: SearchUiState.HasBooks,
    onClickBook: (BookModel) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        state = state
    ) {
        uiState.books.forEach {
            item {
                BookRow(book = it, onClickBook = onClickBook)
            }
        }
    }
}

@Composable
private fun NoBooksContent(
    uiState: SearchUiState.NoBooks,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "${uiState.keyword}에 대한 결과가 없습니다.",
            modifier = Modifier.align(alignment = Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorContent(
    uiState: SearchUiState.Error,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val errorText = when(uiState.exception) {
            AppException.Network -> "네트워크 에러"
        }
        Text(
            text = errorText,
            modifier = Modifier.align(alignment = Alignment.Center),
            textAlign = TextAlign.Center
        )
    }

}

@Preview("Search Screen")
@Composable
fun PreviewSearchScreen() {
    val dummyBooks = (0 until 100)
        .map {
            BookModel(
                id = it.toLong(),
                thumbUrl = "",
                title = "Sample Book",
                content = "Sample Content",
                authors = listOf("Arthur", "Hwang"),
                translators = listOf("김번역"),
                publisher = "파주 출판사",
                price = 1000L,
                released = System.currentTimeMillis(),
                isFavorite = true
            )
        }
    JetnewsTheme {
        SearchScreen(
            uiState = SearchUiState.HasBooks(
                books = dummyBooks,
                isLoading = false,
                keyword = ""
            ),
            modifier = Modifier,
            onSearchKeywordChanged = {},
            onClickBook = {}
        )
    }
}

@Preview("Search Screen NoBook")
@Composable
fun PreviewSearchScreenNoBook() {
    JetnewsTheme {
        SearchScreen(
            uiState = SearchUiState.NoBooks(
                isLoading = false,
                keyword = "NO BOOK~!"
            ),
            modifier = Modifier,
            onSearchKeywordChanged = {},
            onClickBook = {}
        )
    }
}

@Preview("Search Screen Error")
@Composable
fun PreviewSearchScreenError() {
    JetnewsTheme {
        SearchScreen(
            uiState = SearchUiState.Error(
                isLoading = false,
                keyword = "Error BOOK~!",
                exception = AppException.Network
            ),
            modifier = Modifier,
            onSearchKeywordChanged = {},
            onClickBook = {}
        )
    }
}