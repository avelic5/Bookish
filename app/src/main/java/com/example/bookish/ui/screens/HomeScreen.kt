package com.example.bookish.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookish.ui.components.BookCard
import com.example.bookish.data.BookStaticData
import androidx.compose.foundation.lazy.grid.items

@Composable
fun HomeScreen(navController: NavController, initialSearch: String = "") {
    val booksList = remember { BookStaticData.getSampleBooks() }
    var searchQuery by remember { mutableStateOf(initialSearch) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by title") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { /* Optionally filter */ }) {
                Text("Search")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
            items(booksList) { book ->
                BookCard(book = book) {
                    navController.navigate("details/${book.title}")
                }
            } })
    }
}