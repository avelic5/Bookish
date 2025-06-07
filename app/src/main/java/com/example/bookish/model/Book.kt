package com.example.bookish.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation



data class Book(
    @Embedded val book: BookEntity,

    @Relation(
        parentColumn = "id",
        entity = Author::class,
        entityColumn = "authorId",
        associateBy = Junction(
            value = BookAuthorCrossRef::class,
            parentColumn = "bookId",
            entityColumn = "authorId"
        )
    )
    val authors: List<Author>,

    @Relation(
        parentColumn = "id",
        entity = Category::class,
        entityColumn = "categoryId",
        associateBy = Junction(
            value = BookCategoryCrossRef::class,
            parentColumn = "bookId",
            entityColumn = "categoryId"
        )
    )
    val categories: List<Category>
)
