package com.example.bookish.repositories

import com.example.bookish.data.RetrofitInstance
import com.example.bookish.dto.toBook
import com.example.bookish.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object BookRepository {

    // Funkcija za parsiranje JSON odgovora u listu Book objekata
    private fun parseBooksFromJson(json: String): List<Book> {
        val jsonObj = JSONObject(json)
        val itemsArray: JSONArray = jsonObj.optJSONArray("items") ?: return emptyList()

        return List(10) { i ->
            val item = itemsArray.getJSONObject(i)
            val volumeInfo = item.getJSONObject("volumeInfo")
            val imageLinks = volumeInfo.optJSONObject("imageLinks")
            val thumbnailUrl = imageLinks?.optString("thumbnail", "")
            //?.replace("http://", "https://") //
            Book(
                id = item.optString("id", "N/A"),
                title = volumeInfo.optString("title", "N/A"),
                authors = volumeInfo.optJSONArray("authors")?.let { arr ->
                    List(arr.length()) { j -> arr.getString(j) }
                } ?: listOf("Unknown"),
                publisher = volumeInfo.optString("publisher", "Unknown"),
                categories = volumeInfo.optJSONArray("categories")?.let { arr ->
                    List(arr.length()) { j -> arr.getString(j) }
                } ?: listOf("Uncategorized"),
                infoLink = volumeInfo.optString("infoLink", ""),
                description = volumeInfo.optString("description", "No description"),
                thumbnail = thumbnailUrl
            )
        }
    }
    suspend fun getBookById(id: String): Book? = withContext(Dispatchers.IO) {
        val response = api.searchBooks("id:$id")
        response.items.firstOrNull { it.id == id }?.toBook()
    }

    private val api = RetrofitInstance.api

    suspend fun searchBooks(query: String): List<Book> = withContext(Dispatchers.IO) {
        val response = api.searchBooks(query)
        response.items.map { it.toBook() }.take(10)
    }

}