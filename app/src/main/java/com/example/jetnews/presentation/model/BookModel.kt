package com.example.jetnews.presentation.model

data class BookModel(
    val id: String,
    val thumbUrl: String,
    val title: String,
    val content: String,
    val authors: List<String>,
    val translators: List<String>,
    val publisher: String,
    val price: Long,
    val released: Long,
    val isFavorite: Boolean,
)