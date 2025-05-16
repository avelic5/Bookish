package com.example.bookish.ui.screens

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bookish.model.Book
import com.example.bookish.repositories.BookRepository
import com.example.bookish.R
@Composable
fun BookDetailsScreen(id: String, onBack: () -> Unit) {
    val context = LocalContext.current
    var book by remember { mutableStateOf<Book?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(id) {
        isLoading = true
        error = null
        try {
            book = BookRepository.getBookById(id)
            if (book == null) error = "Book not found."
        } catch (e: Exception) {
            error = "Error loading book."
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onBack) { Text("Back") }
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(error ?: "Unknown error", color = Color.Red)
            book != null -> {
                val it = book!!
                Image(
                    painter = painterResource(id = com.example.bookish.R.drawable.books),
                    contentDescription = "Book cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = it.title ?: "Unknown Title",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !it.title.isNullOrBlank()) {
                            val query = "${it.title.orEmpty()} ${it.authors?.joinToString(" ").orEmpty()}"
                            if (query.isNotBlank()) {
                                try {
                                    val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                                        putExtra(SearchManager.QUERY, query)
                                    }
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                        },
                    textAlign = TextAlign.Center
                )
                Text(
                    "Authors: ${it.authors?.joinToString(", ") ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("Publisher: ${it.publisher ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Categories: ${it.categories?.joinToString(", ") ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = it.infoLink ?: "No Info Link",
                    color = Color.Blue,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable(enabled = !it.infoLink.isNullOrBlank()) {
                        val link = it.infoLink
                        if (!link.isNullOrBlank()) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        }
                    }
                )
                Text(
                    text = it.description ?: "No Description",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.clickable(enabled = !it.description.isNullOrBlank()) {
                        val desc = it.description
                        if (!desc.isNullOrBlank()) {
                            try {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, desc)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(intent, "Share via"))
                            } catch (_: Exception) {}
                        }
                    }
                )
            }
        }
    }
}