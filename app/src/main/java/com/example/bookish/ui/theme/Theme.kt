package com.example.bookish.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definicija boja
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD4AF37), // Zlatna boja za primarne elemente
    secondary = Color(0xFF8B5E3C), // Braon nijansa
    background = Color(0xFF121212), // Tamna pozadina
    surface = Color(0xFF1E1E1E), // Površine u tamnom modu
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF8B5E3C), // Braon boja za primarne elemente
    secondary = Color(0xFFD4AF37), // Zlatna nijansa
    background = Color(0xFFFFFBF5), // Svetla bež pozadina
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun BookishTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
