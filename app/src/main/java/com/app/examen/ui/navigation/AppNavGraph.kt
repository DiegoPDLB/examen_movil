package com.app.examen.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.examen.ui.screens.config.ConfigurationScreen
import com.app.examen.ui.screens.puzzle.PuzzleScreen
import com.app.examen.viewmodel.SudokuViewModel

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: SudokuViewModel) {
    NavHost(navController = navController, startDestination = "configuration") {

        composable("configuration") {
            ConfigurationScreen(navController) { width, height, difficulty ->
                viewModel.loadSudoku(width, height, difficulty)
                navController.navigate("puzzle")
            }
        }

        composable("puzzle") {
            PuzzleScreen(viewModel)
        }
    }
}
