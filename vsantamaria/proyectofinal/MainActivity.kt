package com.vsantamaria.proyectofinal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.vsantamaria.proyectofinal.database.AppDatabase
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.navigation.Navigation
import com.vsantamaria.proyectofinal.ui.AppContent

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent {
                val db = AppDatabase.getDatabase(applicationContext)
                val navController = rememberNavController()
                val usersDAO = db.usersDao()
                val usersViewModel = UsersViewModel(usersDAO)
                Navigation(navController = navController, usersViewModel = usersViewModel)
            }
        }
    }
}

