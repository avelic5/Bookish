package com.example.bookish.repositories

import android.content.Context
import com.example.bookish.data.AppDatabase
import com.example.bookish.data.BookDao
import com.example.bookish.data.RetrofitInstance
import com.example.bookish.dto.BookItem
import com.example.bookish.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object BookRepository {
    private val api = RetrofitInstance.api
    private lateinit var dao: BookDao

    fun init(context: Context) {
        dao = AppDatabase.getDatabase(context).bookDao()
    }
    // Za Retrofit DTO BookItem (iz tvoje API definicije)
    private fun bookItemToBook(item: BookItem): Book {
        val bookEntity = BookEntity(
            id = item.id ?: "N/A",
            title = item.volumeInfo.title ?: "N/A",
            publisher = item.volumeInfo.publisher ?: "Unknown",
            infoLink = item.volumeInfo.infoLink ?: "",
            description = item.volumeInfo.description ?: "No description",
            thumbnail = item.volumeInfo.imageLinks?.thumbnail ?: ""
        )

        val authorsList = item.volumeInfo.authors?.map { Author(name = it) } ?: listOf(Author(name = "Unknown"))
        val categoriesList = item.volumeInfo.categories?.map { Category(name = it) } ?: listOf(Category(name = "Uncategorized"))

        return Book(
            book = bookEntity,
            authors = authorsList,
            categories = categoriesList
        )
    }

    suspend fun searchBooks(query: String): List<Book> = withContext(Dispatchers.IO) {
        val response = api.searchBooks(query)
        response.items.mapNotNull { item -> bookItemToBook(item) }.take(10)
    }

    suspend fun getBookById(id: String): Book? = withContext(Dispatchers.IO) {
        val response = api.getBookById(id)
        response?.let { bookItemToBook(it) }
    }

    // Za ručni JSON parsing (koristi se kod searchBooks2 i getBookById2)
    private fun jsonObjectToBook(item: JSONObject): Book? {
        val volumeInfo = item.optJSONObject("volumeInfo") ?: return null
        val imageLinks = volumeInfo.optJSONObject("imageLinks")
        val thumbnailUrl = imageLinks?.optString("thumbnail", "") ?: ""

        val id = item.optString("id", "N/A")

        val bookEntity = BookEntity(
            id = id,
            title = volumeInfo.optString("title", "N/A"),
            publisher = volumeInfo.optString("publisher", "Unknown"),
            infoLink = volumeInfo.optString("infoLink", ""),
            description = volumeInfo.optString("description", "No description"),
            thumbnail = thumbnailUrl
        )

        val authorsList = volumeInfo.optJSONArray("authors")?.let { arr ->
            (0 until arr.length()).map { j -> Author(name = arr.optString(j, "Unknown")) }
        } ?: listOf(Author(name = "Unknown"))

        val categoriesList = volumeInfo.optJSONArray("categories")?.let { arr ->
            (0 until arr.length()).map { j -> Category(name = arr.optString(j, "Uncategorized")) }
        } ?: listOf(Category(name = "Uncategorized"))

        return Book(
            book = bookEntity,
            authors = authorsList,
            categories = categoriesList
        )
    }

    suspend fun searchBooks2(query: String): List<Book> = withContext(Dispatchers.IO) {
        val apiUrl = "https://www.googleapis.com/books/v1/volumes?q=${query}"
        val url = URL(apiUrl)
        val connection = url.openConnection() as? HttpURLConnection
        val response = connection?.inputStream?.bufferedReader()?.use { it.readText() }
        connection?.disconnect()
        response?.let { parseBooksFromJson(it) } ?: emptyList()
    }

    private fun parseBooksFromJson(json: String): List<Book> {
        val jsonObj = JSONObject(json)
        val itemsArray: JSONArray = jsonObj.optJSONArray("items") ?: return emptyList()
        return (0 until minOf(10, itemsArray.length())).mapNotNull { i ->
            jsonObjectToBook(itemsArray.getJSONObject(i))
        }
    }

    suspend fun getBookById2(id: String): Book? = withContext(Dispatchers.IO) {
        val url = URL("https://www.googleapis.com/books/v1/volumes/$id")
        val connection = url.openConnection() as? HttpURLConnection
        val response = connection?.inputStream?.bufferedReader()?.use { it.readText() }
        connection?.disconnect()
        response?.let {
            val item = JSONObject(it)
            jsonObjectToBook(item)
        }
    }

    suspend fun getAllBooksFromDatabase(): List<Book> = withContext(Dispatchers.IO) {
        dao.getAllBooksWithRelations()
    }
    suspend fun removeBookFromDatabase(id: String) = withContext(Dispatchers.IO) {
        dao.deleteBookById(id)
    }

    suspend fun getBookByIdDatabase(id: String): Book? = withContext(Dispatchers.IO) {
        dao.getBookWithRelations(id)
    }
    suspend fun saveToLocal(book: Book) = withContext(Dispatchers.IO) {
        dao.insertBook(book.book)
        val authorIds = dao.insertAuthors(book.authors)
        val categoryIds = dao.insertCategories(book.categories)

        val authorRefs = authorIds.map { BookAuthorCrossRef(book.book.id, it) }
        val categoryRefs = categoryIds.map { BookCategoryCrossRef(book.book.id, it) }

        dao.insertBookAuthorCrossRefs(authorRefs)
        dao.insertBookCategoryCrossRefs(categoryRefs)
    }
}