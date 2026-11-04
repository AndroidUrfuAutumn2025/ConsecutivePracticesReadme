package ru.urfumobile.rickandmorty.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.urfumobile.rickandmorty.data.mockCharacters
import ru.urfumobile.rickandmorty.model.Character

class CharacterDetailsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val character: Character? = savedStateHandle.get<Int>("characterId")?.let { id ->
        mockCharacters.find { it.id == id }
    }
}
