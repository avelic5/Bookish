package com.example.bookish.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val publisher: String,
    val infoLink: String,
    val description: String,
    val thumbnail: String
)

@Entity(tableName = "authors")
data class Author(
    @PrimaryKey(autoGenerate = true) val authorId: Long = 0,
    val name: String
)

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val name: String
)

@Entity(primaryKeys = ["bookId", "authorId"])
data class BookAuthorCrossRef(
    val bookId: String,
    val authorId: Long
)

@Entity(primaryKeys = ["bookId", "categoryId"])
data class BookCategoryCrossRef(
    val bookId: String,
    val categoryId: Long
)