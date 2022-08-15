package com.example.jetnews.data

import android.content.Context
import androidx.room.Room
import com.example.jetnews.data.api.BookApiService
import com.example.jetnews.data.database.BookDatabase
import com.example.jetnews.data.repository.BookRepositoryImpl
import com.example.jetnews.domain.repository.BookRepository
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// TODO - Dagger
interface DataServiceLocator {
    val repository: BookRepository
}

class DataServiceLocatorImpl(
    applicationContext: Context
) : DataServiceLocator {

    private val apiProvider: ApiProvider by lazy {
        ApiProvider(applicationContext)
    }

    private val database: BookDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            BookDatabase::class.java, "book_database.db"
        ).build()
    }

    override val repository: BookRepository by lazy {
        BookRepositoryImpl(
            database = database,
            api = apiProvider.bookService
        )
    }
}

private class ApiProvider(
    context: Context
) {
    companion object {
        private const val CONNECT_TIMEOUT = 15L
        private const val WRITE_TIMEOUT = 15L
        private const val READ_TIMEOUT = 15L

        private const val BASE_URL_KAKAO_BOOK_API = "https://dapi.kakao.com"
    }

    private val cache: Cache by lazy {
        Cache(context.cacheDir, 10L * 1024 * 1024)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            cache(cache)
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Authorization", "KakaoAK 31b9d38adf1b48955225f9ca3686ead8")
                    .build()
                it.proceed(request)
            }
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }

    val bookService: BookApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_KAKAO_BOOK_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(BookApiService::class.java)
    }

}