package com.example.algorithmvisualizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.algorithmvisualizer.ui.home.HomeScreen
import com.example.algorithmvisualizer.ui.searching_alg.SearchingScreen
import com.example.algorithmvisualizer.ui.sorting_alg.SortingListScreen
import com.example.algorithmvisualizer.ui.theme.AlgorithmVisualizerTheme
import com.example.algorithmvisualizer.ui.array_insertion.AddArrayScreen

object Routes {
    const val HOME = "home"
    const val SORTING = "sorting"
    const val SEARCHING = "searching"
    const val ADD_ARRAY = "add_array"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlgorithmVisualizerTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Routes.HOME,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Routes.HOME) {
                                HomeScreen(navController)
                            }
                            composable(Routes.SORTING) {
                                SortingListScreen(
                                    onNavigateBack = { navController.navigateUp() }
                                )
                            }
                            composable(Routes.SEARCHING) {
                                SearchingScreen(
                                    onNavigateBack = { navController.navigateUp() }
                                )
                            }
                            composable(Routes.ADD_ARRAY) {
                                AddArrayScreen(
                                    onNavigateBack = { navController.navigateUp() }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
