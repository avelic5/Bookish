package com.example.bookish.dto

import com.example.bookish.model.Book
import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("items") val items: List<BookItem>
)

data class BookItem(
    @SerializedName("id") val id: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    @SerializedName("title") val title: String?,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("categories") val categories: List<String>?,
    @SerializedName("infoLink") val infoLink: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("imageLinks") val imageLinks: ImageLinks?
)

data class ImageLinks(
    @SerializedName("thumbnail") val thumbnail: String?
)

fun BookItem.toBook(): Book {
    return Book(
        id = this.id,
        title = volumeInfo.title ?: "N/A",
        authors = volumeInfo.authors ?: listOf("Unknown"),
        publisher = volumeInfo.publisher ?: "Unknown",
        categories = volumeInfo.categories ?: listOf("Uncategorized"),
        infoLink = volumeInfo.infoLink ?: "",
        description = volumeInfo.description ?: "No description",
        thumbnail = volumeInfo.imageLinks?.thumbnail ?: ""
    )
}