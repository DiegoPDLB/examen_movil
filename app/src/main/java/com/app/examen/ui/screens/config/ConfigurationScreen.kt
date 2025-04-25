package com.app.examen.ui.screens.config

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ConfigurationScreen(
    navController: NavController,
    onStartGame: (Int, Int, String) -> Unit
) {
    var selectedSize by remember { mutableStateOf("9x9") }
    var selectedDifficulty by remember { mutableStateOf("easy") }

    val sizes = listOf("4x4", "9x9")
    val difficulties = listOf("easy", "medium", "hard")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tamaño del Sudoku", style = MaterialTheme.typography.headlineSmall)

        sizes.forEach { size ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedSize == size,
                        onClick = { selectedSize = size }
                    )
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedSize == size,
                    onClick = { selectedSize = size }
                )
                Text(size, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Text("Dificultad", style = MaterialTheme.typography.headlineSmall)

        difficulties.forEach { level ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedDifficulty == level,
                        onClick = { selectedDifficulty = level }
                    )
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedDifficulty == level,
                    onClick = { selectedDifficulty = level }
                )
                Text(level.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val (width, height) = if (selectedSize == "4x4") 4 to 4 else 9 to 9
                onStartGame(width, height, selectedDifficulty)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Empezar juego", style = MaterialTheme.typography.labelLarge)
        }
    }
}
