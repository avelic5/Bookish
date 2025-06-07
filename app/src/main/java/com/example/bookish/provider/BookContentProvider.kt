package com.example.bookish.provider

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.BaseColumns
import com.example.bookish.data.AppDatabase
import com.example.bookish.data.BookDao
import com.example.bookish.model.*
import kotlinx.coroutines.runBlocking

class BookContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.bookish.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")
        const val BOOKS = 1
        const val BOOKS_ID = 2

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "books", BOOKS)
            addURI(AUTHORITY, "books/*", BOOKS_ID)
        }
    }

    private lateinit var dao: BookDao

    override fun onCreate(): Boolean {
        context?.let {
            dao = AppDatabase.getDatabase(it).bookDao()
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = runBlocking {
        when (uriMatcher.match(uri)) {
            BOOKS -> {
                val books = dao.getAllBooksWithRelations()
                val cursor = MatrixCursor(
                    arrayOf(
                        BaseColumns._ID, "id", "title", "publisher",
                        "infoLink", "description", "thumbnail", "authors", "categories"
                    )
                )
                books.forEachIndexed { index, book ->
                    cursor.addRow(
                        arrayOf(
                            index,
                            book.book.id,
                            book.book.title,
                            book.book.publisher,
                            book.book.infoLink,
                            book.book.description,
                            book.book.thumbnail,
                            book.authors.joinToString(",") { it.name },
                            book.categories.joinToString(",") { it.name }
                        )
                    )
                }
                cursor
            }

            BOOKS_ID -> {
                val id = uri.lastPathSegment ?: return@runBlocking null
                val book = dao.getBookWithRelations(id) ?: return@runBlocking null
                MatrixCursor(
                    arrayOf(
                        BaseColumns._ID, "id", "title", "publisher",
                        "infoLink", "description", "thumbnail", "authors", "categories"
                    )
                ).apply {
                    addRow(
                        arrayOf(
                            0,
                            book.book.id,
                            book.book.title,
                            book.book.publisher,
                            book.book.infoLink,
                            book.book.description,
                            book.book.thumbnail,
                            book.authors.joinToString(",") { it.name },
                            book.categories.joinToString(",") { it.name }
                        )
                    )
                }
            }

            else -> throw IllegalArgumentException("Nepoznat URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = runBlocking {
        when (uriMatcher.match(uri)) {
            BOOKS -> {
                val fullBook = values?.toFullBook() ?: return@runBlocking null
                dao.insertBook(fullBook.book)
                dao.insertAuthors(fullBook.authors)
                dao.insertCategories(fullBook.categories)

                dao.insertBookAuthorCrossRefs(
                    fullBook.authors.map { BookAuthorCrossRef(fullBook.book.id, it.authorId) }
                )
                dao.insertBookCategoryCrossRefs(
                    fullBook.categories.map { BookCategoryCrossRef(fullBook.book.id, it.categoryId) }
                )

                ContentUris.withAppendedId(uri, fullBook.book.id.hashCode().toLong())
            }

            else -> throw IllegalArgumentException("Nepoznat URI za insert: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = runBlocking {
        when (uriMatcher.match(uri)) {
            BOOKS_ID -> {
                val id = uri.lastPathSegment ?: return@runBlocking 0
                val book = dao.getBookEntityById(id) ?: return@runBlocking 0
                dao.deleteAuthorCrossRefs(id)
                dao.deleteCategoryCrossRefs(id)
                dao.deleteBook(book)
                1
            }

            else -> 0
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Update nije podržan")
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            BOOKS -> "vnd.android.cursor.dir/vnd.$AUTHORITY.books"
            BOOKS_ID -> "vnd.android.cursor.item/vnd.$AUTHORITY.books"
            else -> throw IllegalArgumentException("Nepoznat URI: $uri")
        }
    }

    // Pomoćna metoda za parsiranje kompletne knjige iz ContentValues
    private fun ContentValues.toFullBook(): Book {
        val bookEntity = BookEntity(
            id = getAsString("id"),
            title = getAsString("title"),
            publisher = getAsString("publisher"),
            infoLink = getAsString("infoLink"),
            description = getAsString("description"),
            thumbnail = getAsString("thumbnail")
        )

        val authors = getAsString("authors")
            ?.split(",")
            ?.map { Author(name = it.trim()) }
            ?: emptyList()

        val categories = getAsString("categories")
            ?.split(",")
            ?.map { Category(name = it.trim()) }
            ?: emptyList()

        return Book(bookEntity, authors, categories)
    }
}
