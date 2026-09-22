@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ru.urfumobile.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RickAndMortyApp() }
    }
}

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: String,
    val location: String,
    val episodes: Int,
    val color: Color
)

data class AppUiState(
    val characters: List<Character> = mockCharacters,
    val query: String = ""
)

class CharactersViewModel : ViewModel() {
    var uiState by mutableStateOf(AppUiState())
        private set

    val filteredCharacters: List<Character>
        get() = uiState.characters.filter {
            it.name.contains(uiState.query.trim(), ignoreCase = true)
        }

    fun updateQuery(query: String) {
        uiState = uiState.copy(query = query)
    }
}

private enum class Tab(val title: String) {
    CHARACTERS("Персонажи"),
    LOCATIONS("Локации"),
    EPISODES("Эпизоды")
}

private const val catalogRoute = "catalog"
private const val characterRoute = "character/{characterId}"

private fun characterRoute(characterId: Int) = "character/$characterId"

@Composable
fun RickAndMortyApp(viewModel: CharactersViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(Tab.CHARACTERS) }
    val navController = rememberNavController()

    MaterialTheme(colorScheme = RickAndMortyPalette.scheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = RickAndMortyPalette.scheme.background) {
            NavHost(navController = navController, startDestination = catalogRoute) {
                composable(catalogRoute) {
                    Scaffold(
                        containerColor = RickAndMortyPalette.scheme.background,
                        bottomBar = {
                            AppBottomBar(
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it }
                            )
                        }
                    ) { padding ->
                        when (selectedTab) {
                            Tab.CHARACTERS -> CharactersScreen(
                                state = viewModel.uiState,
                                characters = viewModel.filteredCharacters,
                                onQueryChanged = viewModel::updateQuery,
                                onCharacterClick = { character ->
                                    navController.navigate(characterRoute(character.id))
                                },
                                modifier = Modifier.padding(padding)
                            )
                            Tab.LOCATIONS -> PlaceholderScreen(
                                title = "Локации",
                                subtitle = "Здесь будут планеты и измерения сериала",
                                icon = Icons.Default.LocationOn,
                                modifier = Modifier.padding(padding)
                            )
                            Tab.EPISODES -> PlaceholderScreen(
                                title = "Эпизоды",
                                subtitle = "Здесь появится список эпизодов",
                                icon = Icons.Default.Tv,
                                modifier = Modifier.padding(padding)
                            )
                        }
                    }
                }
                composable(
                    route = characterRoute,
                    arguments = listOf(navArgument("characterId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val characterId = backStackEntry.arguments?.getInt("characterId")
                    val character = viewModel.uiState.characters.firstOrNull { it.id == characterId }

                    character?.let {
                        CharacterDetails(
                            character = it,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CharactersScreen(
    state: AppUiState,
    characters: List<Character>,
    onQueryChanged: (String) -> Unit,
    onCharacterClick: (Character) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Rick and Morty",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = RickAndMortyPalette.scheme.onBackground
                    )
                    Text(
                        text = "Гид по вселенным сериала",
                        fontSize = 12.sp,
                        color = RickAndMortyPalette.muted
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Избранное",
                        tint = RickAndMortyPalette.scheme.onBackground
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Персонажи",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = RickAndMortyPalette.scheme.onBackground
            )
            Text(
                text = "Найдено персонажей: ${characters.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = RickAndMortyPalette.muted,
                modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
            )
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Поиск")
                },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChanged("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Очистить поиск")
                        }
                    }
                },
                placeholder = { Text("Поиск по имени") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (characters.isEmpty()) {
            EmptySearchState(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(characters, key = { it.id }) { character ->
                    CharacterListItem(character = character, onClick = { onCharacterClick(character) })
                }
            }
        }
    }
}

@Composable
private fun CharacterListItem(character: Character, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RickAndMortyPalette.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CharacterAvatar(character = character, size = 58.dp)
            Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RickAndMortyPalette.scheme.onBackground
                )
                Text(
                    text = "${character.species} • ${character.gender}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RickAndMortyPalette.muted,
                    modifier = Modifier.padding(top = 3.dp)
                )
                StatusLabel(status = character.status)
            }
            Text(
                text = "›",
                fontSize = 28.sp,
                color = RickAndMortyPalette.muted,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetails(character: Character, onBack: () -> Unit) {
    Scaffold(
        containerColor = RickAndMortyPalette.scheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Профиль персонажа", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CharacterAvatar(character = character, size = 128.dp)
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = RickAndMortyPalette.scheme.onBackground,
                    modifier = Modifier.padding(top = 12.dp)
                )
                StatusLabel(status = character.status, large = true)
                Text(
                    text = "Персонаж #${character.id}",
                    color = RickAndMortyPalette.muted,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            InfoSection(title = "Основная информация") {
                InfoRow("Вид", character.species)
                InfoRow("Пол", character.gender)
                InfoRow("Тип", character.type.ifBlank { "Не указан" })
                InfoRow("Происхождение", character.origin)
                InfoRow("Последняя локация", character.location)
                InfoRow("Появился в эпизодах", character.episodes.toString())
            }

            AssistChip(
                onClick = { },
                label = { Text("Добавить в избранное") },
                leadingIcon = {
                    Icon(Icons.Default.BookmarkBorder, contentDescription = null)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp, bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun InfoSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = RickAndMortyPalette.scheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = RickAndMortyPalette.card)
        ) { content() }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = RickAndMortyPalette.muted)
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = RickAndMortyPalette.scheme.onBackground,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
    HorizontalDivider(color = RickAndMortyPalette.divider)
}

@Composable
private fun StatusLabel(status: String, large: Boolean = false) {
    val isAlive = status == "Живой"
    val statusColor = if (isAlive) RickAndMortyPalette.success else RickAndMortyPalette.muted
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = if (large) 8.dp else 7.dp)
    ) {
        Box(modifier = Modifier.size(if (large) 9.dp else 7.dp).clip(CircleShape).background(statusColor))
        Text(
            text = status,
            color = statusColor,
            style = if (large) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
private fun CharacterAvatar(character: Character, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(size / 4))
            .background(character.color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = character.name.split(" ").take(2).joinToString("") { it.first().toString() },
            color = Color.White,
            fontSize = (size.value / 3.7f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptySearchState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(42.dp),
            tint = RickAndMortyPalette.muted
        )
        Text(
            text = "Ничего не найдено",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = "Попробуйте изменить запрос",
            color = RickAndMortyPalette.muted,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(56.dp), tint = RickAndMortyPalette.accent)
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
        Text(subtitle, color = RickAndMortyPalette.muted, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun AppBottomBar(selectedTab: Tab, onTabSelected: (Tab) -> Unit) {
    BottomAppBar(
        containerColor = RickAndMortyPalette.card,
        modifier = Modifier.navigationBarsPadding()
    ) {
        NavigationBarItem(
            selected = selectedTab == Tab.CHARACTERS,
            onClick = { onTabSelected(Tab.CHARACTERS) },
            icon = { Icon(Icons.Default.Home, contentDescription = Tab.CHARACTERS.title) },
            label = { Text(Tab.CHARACTERS.title) }
        )
        NavigationBarItem(
            selected = selectedTab == Tab.LOCATIONS,
            onClick = { onTabSelected(Tab.LOCATIONS) },
            icon = { Icon(Icons.Default.LocationOn, contentDescription = Tab.LOCATIONS.title) },
            label = { Text(Tab.LOCATIONS.title) }
        )
        NavigationBarItem(
            selected = selectedTab == Tab.EPISODES,
            onClick = { onTabSelected(Tab.EPISODES) },
            icon = { Icon(Icons.Default.Tv, contentDescription = Tab.EPISODES.title) },
            label = { Text(Tab.EPISODES.title) }
        )
    }
}

private object RickAndMortyPalette {
    val scheme = androidx.compose.material3.lightColorScheme(
        background = Color(0xFFF4F7F2),
        surface = Color(0xFFF4F7F2),
        onBackground = Color(0xFF1B241E),
        onSurface = Color(0xFF1B241E),
        primary = Color(0xFF3D7654),
        onPrimary = Color.White,
        secondary = Color(0xFFB9D8BF),
        outline = Color(0xFFB8C6BA)
    )
    val card = Color(0xFFFFFFFF)
    val muted = Color(0xFF66736A)
    val divider = Color(0xFFE4EAE4)
    val success = Color(0xFF33824A)
    val accent = Color(0xFF3D7654)
}

private val mockCharacters = listOf(
    Character(1, "Рик Санчез", "Живой", "Человек", "", "Мужской", "Земля (C-137)", "Цитадель Риков", 71, Color(0xFF5C9AC6)),
    Character(2, "Морти Смит", "Живой", "Человек", "", "Мужской", "Земля (C-137)", "Земля (C-137)", 71, Color(0xFFE2B35D)),
    Character(3, "Саммер Смит", "Живой", "Человек", "", "Женский", "Земля (С-137)", "Земля (С-137)", 51, Color(0xFFE27D8D)),
    Character(4, "Бет Смит", "Живой", "Человек", "", "Женский", "Земля (С-137)", "Земля (С-137)", 59, Color(0xFF9B76B8)),
    Character(5, "Джерри Смит", "Живой", "Человек", "", "Мужской", "Земля (С-137)", "Земля (С-137)", 60, Color(0xFF7B9C73)),
    Character(6, "Мистер Мисикс", "Живой", "Мисикс", "", "Мужской", "Измерение Мисиксов", "Измерение Мисиксов", 4, Color(0xFF4A9E9A)),
    Character(7, "Пикл Рик", "Живой", "Человек", "", "Мужской", "Земля (С-137)", "Дом семьи Смитов", 9, Color(0xFF78A94F)),
    Character(8, "Птичья Личность", "Мёртв", "Гуманоид", "", "Мужской", "Земля (C-137)", "Земля (C-137)", 13, Color(0xFFB56C4F))
)

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    RickAndMortyApp()
}
