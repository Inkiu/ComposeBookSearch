package com.example.jetnews.data.data

data class BookData(
    val id: String,
    val thumbUrl: String,
    val title: String,
    val content: String,
    val authors: List<String>,
    val translators: List<String>,
    val publisher: String,
    val price: Long,
    val released: Long,
)