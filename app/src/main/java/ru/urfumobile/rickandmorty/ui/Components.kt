package ru.urfumobile.rickandmorty.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.urfumobile.rickandmorty.model.Character

@Composable
fun CharacterAvatar(character: Character, size: Dp) {
    val color = AppColors.avatarColors[(character.id - 1) % AppColors.avatarColors.size]
    Box(
        modifier = Modifier.size(size).clip(RoundedCornerShape(size / 4)).background(color),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = character.name.split(" ").take(2).joinToString("") { it.first().toString() },
            color = Color.White,
            fontSize = (size.value / 3.7f).sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun StatusLabel(status: String, large: Boolean = false) {
    val color = if (status == "Живой") AppColors.success else AppColors.muted
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = if (large) 8.dp else 7.dp),
    ) {
        Box(Modifier.size(if (large) 9.dp else 7.dp).clip(CircleShape).background(color))
        Text(
            text = status,
            color = color,
            style = if (large) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 5.dp),
        )
    }
}
