package com.vsantamaria.proyectofinal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vsantamaria.proyectofinal.database.daos.GamesDAO
import com.vsantamaria.proyectofinal.database.entities.Games
import com.vsantamaria.proyectofinal.database.viewmodels.GamesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import junit.framework.TestCase.assertEquals

class GamesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockGamesDao: GamesDAO
    private lateinit var gamesViewModel: GamesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockGamesDao = mock()
        gamesViewModel = GamesViewModel(mockGamesDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insertGame should call DAO to insert a game`() {
        val game = Games(1, "Test Game", "Test Developer")

        gamesViewModel.insertGame(game)

        testDispatcher.scheduler.advanceUntilIdle()

        verify(mockGamesDao).insertGame(game)
    }

    @Test
    fun `getAllGames should return the correct list of games`() {
        val mockGames = listOf(
            Games(1, "Game 1", "Developer 1"),
            Games(2, "Game 2", "Developer 2")
        )

        whenever(mockGamesDao.getAllGames()).thenReturn(mockGames)

        val result = gamesViewModel.getAllGames()

        assertEquals(mockGames, result)
    }

    @Test
    fun `deleteGame should call DAO to delete a game by ID`() {
        val gameId = 1

        gamesViewModel.deleteGame(gameId)

        testDispatcher.scheduler.advanceUntilIdle()

        verify(mockGamesDao).deleteGameById(gameId)
    }
}
