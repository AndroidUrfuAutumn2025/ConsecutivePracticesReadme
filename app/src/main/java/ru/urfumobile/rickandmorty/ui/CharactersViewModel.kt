package ru.urfumobile.rickandmorty.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.urfumobile.rickandmorty.data.mockCharacters
import ru.urfumobile.rickandmorty.model.Character

data class CharactersUiState(
    val characters: List<Character> = mockCharacters,
    val query: String = "",
)

class CharactersViewModel : ViewModel() {
    var uiState by mutableStateOf(CharactersUiState())
        private set

    val visibleCharacters: List<Character>
        get() = uiState.characters.filter {
            it.name.contains(uiState.query.trim(), ignoreCase = true)
        }

    fun updateQuery(query: String) {
        uiState = uiState.copy(query = query)
    }

    fun findCharacter(id: Int): Character? = uiState.characters.find { it.id == id }
}
