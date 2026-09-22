package ru.urfumobile.rickandmorty.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColors {
    val card = Color.White
    val muted = Color(0xFF66736A)
    val divider = Color(0xFFE4EAE4)
    val success = Color(0xFF33824A)
    val avatarColors = listOf(
        Color(0xFF5C9AC6), Color(0xFFE2B35D), Color(0xFFE27D8D), Color(0xFF9B76B8),
        Color(0xFF7B9C73), Color(0xFF4A9E9A), Color(0xFF78A94F), Color(0xFFB56C4F),
    )
}

private val appColorScheme = lightColorScheme(
    background = Color(0xFFF4F7F2),
    surface = Color(0xFFF4F7F2),
    onBackground = Color(0xFF1B241E),
    onSurface = Color(0xFF1B241E),
    primary = Color(0xFF3D7654),
    onPrimary = Color.White,
    secondary = Color(0xFFB9D8BF),
    outline = Color(0xFFB8C6BA),
)

@Composable
fun RickAndMortyTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = appColorScheme, content = content)
}
