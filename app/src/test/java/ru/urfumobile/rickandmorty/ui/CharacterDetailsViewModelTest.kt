package ru.urfumobile.rickandmorty.ui

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CharacterDetailsViewModelTest {
    @Test
    fun `details loads the character from the navigation id`() {
        val viewModel = CharacterDetailsViewModel(SavedStateHandle(mapOf("characterId" to 2)))

        assertEquals("Морти Смит", viewModel.character?.name)
    }

    @Test
    fun `unknown id returns no character`() {
        val viewModel = CharacterDetailsViewModel(SavedStateHandle(mapOf("characterId" to 999)))

        assertNull(viewModel.character)
    }

    @Test
    fun `missing navigation id returns no character`() {
        assertNull(CharacterDetailsViewModel(SavedStateHandle()).character)
    }
}
