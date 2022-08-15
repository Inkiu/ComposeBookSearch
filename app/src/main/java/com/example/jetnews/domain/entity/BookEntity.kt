package com.example.jetnews.domain.entity

data class BookEntity(
    val isbn: String,
    val thumbUrl: String,
    val title: String,
    val content: String,
    val authors: List<String>,
    val translators: List<String>,
    val publisher: String,
    val price: Long,
    val released: Long,
    val liked: Boolean,
)