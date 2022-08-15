package com.example.jetnews.data.repository

import androidx.paging.*
import com.example.jetnews.data.api.BookApiService
import com.example.jetnews.data.data.BookData
import com.example.jetnews.data.database.BookDBLikeEntity
import com.example.jetnews.data.database.BookDatabase
import com.example.jetnews.domain.entity.BookEntity
import com.example.jetnews.domain.repository.BookRepository
import com.soochang.data.v2.api.BookApiItemResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.NoSuchElementException

class BookRepositoryImpl(
    database: BookDatabase,
    private val api: BookApiService
) : BookRepository {

    private val cache: MutableList<BookData> = mutableListOf()
    private val likeDao = database.bookLikeEntityDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBookPagingDataFlow(query: String): Flow<PagingData<BookEntity>> {
        cache.clear() // 쿼리가 바뀌었으니 NOTE 개선 필요함
        return likeDao.getAll().flatMapLatest { likeList ->
            Pager(
                config = PagingConfig(pageSize = 50, enablePlaceholders = false),
                pagingSourceFactory = {
                    BookPagingDataSource(query = query, api = api, cache = cache)
                }
            ).flow.map { pagingData ->
                pagingData.map { data ->
                    data.toEntity(liked = likeList.any { it.id == data.id })
                }
            }
        }
    }

    override fun getBook(id: Long): Flow<BookEntity> {
        val data = cache.find { it.id == id }
            ?: throw NoSuchElementException("id $id entity is not exist!")

        return likeDao.getAll()
            .map { likeList ->
                data.toEntity(liked = likeList.any { it.id == data.id })
            }
    }

    override suspend fun updateBookLike(id: Long, like: Boolean) {
        if (like) {
            likeDao.insert(BookDBLikeEntity(id))
        } else {
            likeDao.delete(id)
        }
    }

    private fun BookData.toEntity(liked: Boolean) = BookEntity(
        id = id,
        thumbUrl = thumbUrl,
        title = title,
        content = content,
        authors = authors,
        translators = translators,
        publisher = publisher,
        price = price,
        released = released,
        liked = liked
    )

    internal class BookPagingDataSource(
        private val query: String,
        private val api: BookApiService,
        private val cache: MutableList<BookData>
    ): PagingSource<Int, BookData>() {

        private val dateFormat = SimpleDateFormat(
            "yyyy-MM-DDThh:mm:ss.000+tz",
            Locale.KOREA
        )

        // TODO - STUDY
        override fun getRefreshKey(state: PagingState<Int, BookData>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookData> {
            return kotlin.runCatching {
                val pageSize = params.loadSize
                val requestedPage = params.key ?: 1
                val apiRequest = suspend {
                    api.getBookList(query = query, page = requestedPage, size = pageSize)
                        .documents.map { it.toData(dateFormat) }
                }

                val data = if (requestedPage == 1) {
                    if (cache.size < pageSize) apiRequest() else cache
                } else {
                    apiRequest()
                }.let { data ->
                    if (data.isEmpty()) {
                        data
                    } else {
                        data.subList(0, (data.size % pageSize) * pageSize)
                    }
                }

                LoadResult.Page(
                    data = data,
                    prevKey = null,
                    nextKey = if (data.isEmpty()) null else (data.size % pageSize) + 1
                )
            }.getOrElse {
                LoadResult.Error(it)
            }
        }

        private fun BookApiItemResponse.toData(
            dateFormat: DateFormat
        ) = BookData(
            id = (isbn ?: "0").substringBefore(" ").toLong(),
            thumbUrl = thumbnail ?: "",
            title = title ?: "",
            content = contents ?: "",
            authors = authors ?: emptyList(),
            translators = translators ?: emptyList(),
            publisher = publisher ?: "",
            price = price ?: 0L,
            released = dateFormat.parse(datetime ?: "")?.time ?: 0L,
        )

    }
}