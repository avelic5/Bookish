package com.example.bookish

data class Book(
    val title: String,
    val authors: List<String>,
    val publisher: String,
    val categories: List<String>,
    val infoLink: String,
    val description: String
)