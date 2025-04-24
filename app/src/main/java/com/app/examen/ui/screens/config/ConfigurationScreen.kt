package com.app.examen.ui.screens.config

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ConfigurationScreen(navController: NavController, onStartGame: (Int, Int, String) -> Unit) {
    var selectedSize by remember { mutableStateOf("9x9") }
    var selectedDifficulty by remember { mutableStateOf("easy") }

    val sizes = listOf("4x4", "9x9")
    val difficulties = listOf("easy", "medium", "hard")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecciona el tamaño del Sudoku", style = MaterialTheme.typography.titleMedium)

        sizes.forEach { size ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedSize == size,
                        onClick = { selectedSize = size }
                    )
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedSize == size,
                    onClick = { selectedSize = size }
                )
                Text(size)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona la dificultad", style = MaterialTheme.typography.titleMedium)

        difficulties.forEach { level ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedDifficulty == level,
                        onClick = { selectedDifficulty = level }
                    )
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedDifficulty == level,
                    onClick = { selectedDifficulty = level }
                )
                Text(level)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val (width, height) = if (selectedSize == "4x4") 4 to 4 else 9 to 9
                onStartGame(width, height, selectedDifficulty)
            }
        ) {
            Text("Empezar juego")
        }
    }
}
