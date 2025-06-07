package com.example.bookish.repositories

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import com.example.bookish.data.AppDatabase
import com.example.bookish.data.BookDao
import com.example.bookish.data.RetrofitInstance
import com.example.bookish.dto.BookItem
import com.example.bookish.dto.toBook
import com.example.bookish.model.*
import com.example.bookish.provider.BookContentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class BookRepository(context: Context){
    private val api = RetrofitInstance.api
    private val uri = Uri.parse("content://${BookContentProvider.AUTHORITY}/books")
    private val dao by lazy {
        AppDatabase.getDatabase(context).bookDao()
    }

    suspend fun searchBooks(query: String): List<Book> = withContext(Dispatchers.IO) {
        val response = api.searchBooks(query)
        response.items.map { it.toBook() }.take(10)
    }

    suspend fun getBookById(id: String): Book? = withContext(Dispatchers.IO) {
        val response = api.getBookById(id)
        response.toBook()
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

    suspend fun deleteFromLocal(book: Book) = withContext(Dispatchers.IO) {
        dao.deleteBook(book.book)
    }

    suspend fun getLocalBooks(): List<Book> = withContext(Dispatchers.IO) {
        dao.getAllBooksWithRelations()
    }


    suspend fun loadBooksFromContentProvider(context: Context): List<Book> = withContext(Dispatchers.IO) {

        val cursor = context.contentResolver.query(uri, null, null, null, null)
        val books = mutableListOf<Book>()

        cursor?.use {
            val idIndex = it.getColumnIndex("id")
            val titleIndex = it.getColumnIndex("title")
            val publisherIndex = it.getColumnIndex("publisher")
            val infoLinkIndex = it.getColumnIndex("infoLink")
            val descriptionIndex = it.getColumnIndex("description")
            val thumbnailIndex = it.getColumnIndex("thumbnail")
            val authorsIndex = it.getColumnIndex("authors") // očekuje se da je spojena kolona stringa
            val categoriesIndex = it.getColumnIndex("categories") // isto

            while (it.moveToNext()) {
                val id = it.getString(idIndex)
                val title = it.getString(titleIndex)
                val publisher = it.getString(publisherIndex)
                val infoLink = it.getString(infoLinkIndex)
                val description = it.getString(descriptionIndex)
                val thumbnail = it.getString(thumbnailIndex)
                val authors = it.getString(authorsIndex)
                val categories = it.getString(categoriesIndex)

                books.add(
                    Book(
                        book = BookEntity(id, title, publisher, infoLink, description, thumbnail),
                        authors = authors.split(",").map { Author(name = it.trim()) },
                        categories = categories.split(",").map { Category(name = it.trim()) }
                    )
                )
            }
        }

        books
    }

    suspend fun getBookByIdFromProvider(id: String, context: Context): Book? = withContext(Dispatchers.IO) {
        val bookUri = Uri.withAppendedPath(uri, id)
        val cursor = context.contentResolver.query(bookUri, null, null, null, null) ?: return@withContext null
        cursor.use {
            if (it.moveToFirst()) {
                val book = BookEntity(
                    id = it.getString(it.getColumnIndexOrThrow("id")),
                    title = it.getString(it.getColumnIndexOrThrow("title")),
                    publisher = it.getString(it.getColumnIndexOrThrow("publisher")),
                    infoLink = it.getString(it.getColumnIndexOrThrow("infoLink")),
                    description = it.getString(it.getColumnIndexOrThrow("description")),
                    thumbnail = it.getString(it.getColumnIndexOrThrow("thumbnail"))
                )
                val authors = it.getString(it.getColumnIndexOrThrow("authors"))
                    ?.split(",")
                    ?.map { name -> Author(name = name.trim()) }
                    ?: emptyList()

                val categories = it.getString(it.getColumnIndexOrThrow("categories"))
                    ?.split(",")
                    ?.map { name -> Category(name = name.trim()) }
                    ?: emptyList()

                return@withContext Book(book, authors, categories)
            }
        }
        null
    }
    suspend fun saveToProvider(book: Book, context: Context): Boolean = withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put("id", book.book.id)
            put("title", book.book.title)
            put("publisher", book.book.publisher)
            put("infoLink", book.book.infoLink)
            put("description", book.book.description)
            put("thumbnail", book.book.thumbnail)
            put("authors", book.authors.joinToString(",") { it.name })
            put("categories", book.categories.joinToString(",") { it.name })
        }

        val inserted = context.contentResolver.insert(uri, values)
        return@withContext inserted != null
    }

    suspend fun deleteFromProvider(book: Book, context: Context): Boolean = withContext(Dispatchers.IO) {
        val bookUri = Uri.withAppendedPath(uri, book.book.id)
        val rows = context.contentResolver.delete(bookUri, null, null)
        return@withContext rows > 0
    }
    // Suspendirana funkcija koja izvršava mrežni poziv u IO thread-u
    /*  suspend fun searchBooks2(query: String): List<Book> = withContext(Dispatchers.IO) {
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
                 id = item.optString("id","N/A"),
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
     suspend fun getBookById2(id: String): Book? = withContext(Dispatchers.IO) {
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
 */


}