package com.example.bookish.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookish.data.BookStaticData
import com.example.bookish.ui.screens.BookDetailsScreen
import com.example.bookish.ui.screens.HomeScreen
@Composable
fun BookishNavGraph(startText: String? = null) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, initialSearch = startText ?: "")
        }
        composable(
            "details/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""

            val book = BookStaticData.getSampleBooks()
                .firstOrNull { it.title.equals(title, ignoreCase = true) }

            if (book != null) {
                BookDetailsScreen(book = book, onBack = { navController.popBackStack() })
            } else {
                Text("Book not found")
            }
        }
    }
}