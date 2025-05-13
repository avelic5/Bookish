package com.example.bookish.repositories

import com.example.bookish.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object BookRepository {

    // Suspendirana funkcija koja izvršava mrežni poziv u IO thread-u
    suspend fun searchBooks(query: String): List<Book> = withContext(Dispatchers.IO) {
        val apiUrl = "https://www.googleapis.com/books/v1/volumes?q=${query}"
        val url = URL(apiUrl)

        // Otvaranje konekcije
        val connection = url.openConnection() as? HttpURLConnection
        val response = connection?.inputStream?.bufferedReader()?.use { it.readText() }
        connection?.disconnect()

        // Parsiranje odgovora ako postoji
        response?.let { parseBooksFromJson(it) } ?: emptyList()
    }

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
        val url = URL("https://www.googleapis.com/books/v1/volumes/$id")
        val connection = url.openConnection() as? HttpURLConnection
        val response = connection?.inputStream?.bufferedReader()?.use { it.readText() }
        connection?.disconnect()

        response?.let {
            val item = JSONObject(it)
            val volumeInfo = item.getJSONObject("volumeInfo")
            val imageLinks = volumeInfo.optJSONObject("imageLinks")

            Book(
                id = id,
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
                thumbnail = imageLinks?.optString("thumbnail", "")
            )
        }
    }

}