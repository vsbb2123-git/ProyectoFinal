package com.vsantamaria.proyectofinal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vsantamaria.proyectofinal.database.daos.UsersDAO
import com.vsantamaria.proyectofinal.database.entities.Users
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.mockito.kotlin.verify

class UsersViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockUsersDao: UsersDAO
    private lateinit var usersViewModel: UsersViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockUsersDao = mock() /// Se crea un mock del dao
        usersViewModel = UsersViewModel(mockUsersDao)///se crea el ViewModel con el mock
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain() /// esto es necesario para los tests (menos para el primero)
    }
    @Test
    fun `getAllUsers should return correct data`() {
        val mockUsers = listOf(
            Users(1, "User1", "password1", "base"),
            Users(2, "User2", "password2", "crítico")
        )
        whenever(mockUsersDao.getAllUsers()).thenReturn(mockUsers)
        val users = usersViewModel.getAllUsers()

        assertEquals(2, users.size)
        assertEquals("User1", users[0].username)
    }

    @Test
    fun `insertUser should call DAO to insert user`()= runTest {
        val user = Users(1, "TestUser", "password", "base")

        usersViewModel.insertUser(user)
        testDispatcher.scheduler.advanceUntilIdle()
        verify(mockUsersDao).insertUser(user)
    }

    @Test
    fun `login should call DAO to update current session`() {
        val userId = 1

        usersViewModel.login(userId)
        testDispatcher.scheduler.advanceUntilIdle()
        verify(mockUsersDao).loginUser(userId)
    }
    @Test
    fun `deleteUser should call DAO to delete a user by ID`() {
        val userId = 1

        usersViewModel.deleteUser(userId)

        testDispatcher.scheduler.advanceUntilIdle()

        verify(mockUsersDao).deleteUserById(userId)
    }

    @Test
    fun `getCurrentUser should return the current session user`() {
        val user = Users(1, "TestUser", "password", "base", currentSession = true)

        whenever(mockUsersDao.getCurrentSessionUser()).thenReturn(user)
        testDispatcher.scheduler.advanceUntilIdle()

        val observer = Observer<Users?> { result ->
            assertEquals(user, result)
        }
        usersViewModel.getCurrentUser().observeForever(observer)
        usersViewModel.getCurrentUser().removeObserver(observer)/// Se cierra el observer que si no da problemas
    }///el horror

}
