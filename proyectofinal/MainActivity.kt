package com.vsantamaria.proyectofinal

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.vsantamaria.proyectofinal.database.AppDatabase
import com.vsantamaria.proyectofinal.database.viewmodels.CommentsViewModel
import com.vsantamaria.proyectofinal.database.viewmodels.UsersViewModel
import com.vsantamaria.proyectofinal.navigation.Navigation
import com.vsantamaria.proyectofinal.ui.AppContent

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permiso otorgado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso denegado. Algunas funciones no estarán disponibles.", Toast.LENGTH_SHORT).show()
            }
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1001
        )
        setContent {
            AppContent {
                val db = AppDatabase.getDatabase(applicationContext)
                val navController = rememberNavController()
                val usersDAO = db.usersDao()
                val commentsDAO = db.commentsDao()
                val usersViewModel = UsersViewModel(usersDAO)
                val commentsViewModel = CommentsViewModel(commentsDAO)
                Navigation(navController = navController, usersViewModel = usersViewModel, commentsViewModel = commentsViewModel)
            }
        }
    }
}

