package com.example.bookish.data

import com.example.bookish.dto.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String
    ): BookResponse
}