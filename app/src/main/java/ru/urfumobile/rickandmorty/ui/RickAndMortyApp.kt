@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ru.urfumobile.rickandmorty.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.urfumobile.rickandmorty.model.Character

private enum class Tab(val title: String, val icon: ImageVector) {
    CHARACTERS("Персонажи", Icons.Default.Home),
    LOCATIONS("Локации", Icons.Default.LocationOn),
    EPISODES("Эпизоды", Icons.Default.Tv),
}

private const val catalogRoute = "catalog"
private const val detailsRoute = "character/{characterId}"

@Composable
fun RickAndMortyApp(viewModel: CharactersViewModel = viewModel()) {
    val navController = rememberNavController()

    RickAndMortyTheme {
        Surface(Modifier.fillMaxSize()) {
            NavHost(navController = navController, startDestination = catalogRoute) {
                composable(catalogRoute) {
                    CatalogScreen(
                        viewModel = viewModel,
                        onCharacterClick = { navController.navigate("character/${it.id}") },
                    )
                }
                composable(
                    route = detailsRoute,
                    arguments = listOf(navArgument("characterId") { type = NavType.IntType }),
                ) { entry ->
                    val character = viewModel.findCharacter(entry.arguments?.getInt("characterId") ?: -1)
                    if (character != null) CharacterDetailsScreen(character, navController::popBackStack)
                }
            }
        }
    }
}

@Composable
private fun CatalogScreen(viewModel: CharactersViewModel, onCharacterClick: (Character) -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(Tab.CHARACTERS) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = AppColors.card) {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        label = { Text(tab.title) },
                    )
                }
            }
        },
    ) { padding ->
        when (selectedTab) {
            Tab.CHARACTERS -> CharactersScreen(
                state = viewModel.uiState,
                characters = viewModel.visibleCharacters,
                onQueryChanged = viewModel::updateQuery,
                onCharacterClick = onCharacterClick,
                modifier = Modifier.padding(padding),
            )
            Tab.LOCATIONS -> PlaceholderScreen("Локации", "Здесь будут планеты и измерения", Tab.LOCATIONS, Modifier.padding(padding))
            Tab.EPISODES -> PlaceholderScreen("Эпизоды", "Здесь появится список эпизодов", Tab.EPISODES, Modifier.padding(padding))
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String, subtitle: String, tab: Tab, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(tab.icon, contentDescription = null, modifier = Modifier.size(56.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
        Text(subtitle, color = AppColors.muted, modifier = Modifier.padding(top = 8.dp))
    }
}
