package com.soochang.data.v2.api

import com.google.gson.annotations.SerializedName


data class BookApiResponse(
    @SerializedName("meta") val meta: BookApiMetaResponse,
    @SerializedName("documents") val documents: List<BookApiItemResponse>,
)

data class BookApiMetaResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("is_end") val isEnd: Boolean,
)

class BookApiItemResponse(
    @SerializedName("title") val title: String?,
    @SerializedName("contents") val contents: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("isbn") val isbn: String?,
    @SerializedName("datetime") val datetime: String?,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("translators") val translators: List<String>?,
    @SerializedName("price") val price: Long?,
    @SerializedName("sale_price") val salePrice: Int?,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("status") val status: String?
)