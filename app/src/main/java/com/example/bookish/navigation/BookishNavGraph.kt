package com.example.bookish.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.bookish.data.BookStaticData
import com.example.bookish.model.Book
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
            route = "bookDetails/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            if (id != null) {
                BookDetailsScreen(
                    id = id,
                    onBack = { navController.popBackStack() }
                )
            } else {
                Text("Error: Book ID is missing.")
            }
        }
    }
}
