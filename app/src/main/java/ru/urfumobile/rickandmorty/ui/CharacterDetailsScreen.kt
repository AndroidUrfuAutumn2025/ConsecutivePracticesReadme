@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ru.urfumobile.rickandmorty.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.urfumobile.rickandmorty.model.Character

@Composable
fun CharacterDetailsScreen(character: Character, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль персонажа", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CharacterAvatar(character, 128.dp)
            Text(
                character.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp),
            )
            StatusLabel(character.status, large = true)
            Text("Персонаж #${character.id}", color = AppColors.muted, modifier = Modifier.padding(top = 4.dp))
            CharacterInfoCard(character)
        }
    }
}

@Composable
private fun CharacterInfoCard(character: Character) {
    val fields = listOf(
        "Вид" to character.species,
        "Пол" to character.gender,
        "Тип" to character.type.ifBlank { "Не указан" },
        "Происхождение" to character.origin,
        "Последняя локация" to character.location,
        "Появился в эпизодах" to character.episodeCount.toString(),
    )

    Column(Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
        Text("Основная информация", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AppColors.card),
        ) {
            fields.forEachIndexed { index, (label, value) ->
                Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(label, style = MaterialTheme.typography.labelMedium, color = AppColors.muted)
                    Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                }
                if (index < fields.lastIndex) HorizontalDivider(color = AppColors.divider)
            }
        }
    }
}
