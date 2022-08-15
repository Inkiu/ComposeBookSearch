package com.example.jetnews.domain.repository

import androidx.paging.PagingData
import com.example.jetnews.domain.entity.BookEntity
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getBookPagingDataFlow(query: String): Flow<PagingData<BookEntity>>

    fun getBook(id: String): Flow<BookEntity>

    suspend fun updateBookLike(id: String, like: Boolean)

}