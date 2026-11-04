package ru.urfumobile.rickandmorty.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class CharactersViewModelTest {
    private val viewModel = CharactersViewModel()

    @Test
    fun `search ignores case and surrounding spaces`() {
        viewModel.updateQuery("  МОРТИ ")

        assertEquals(listOf("Морти Смит"), viewModel.visibleCharacters.map { it.name })
    }
}
