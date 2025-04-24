package com.app.examen.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.examen.viewmodel.SudokuUiState
import com.app.examen.viewmodel.SudokuViewModel

@Composable
fun HomeScreen(viewModel: SudokuViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSavedGame()
    }

    when (val state = uiState) {
        is SudokuUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is SudokuUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.message}")
            }
        }

        is SudokuUiState.Success -> {
            var input by remember { mutableStateOf(state.puzzle.map { row -> row.map { it?.toString() ?: "" }.toMutableStateList() }.toMutableStateList()) }
            var message by remember { mutableStateOf<String?>(null) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tablero de Sudoku
                state.puzzle.forEachIndexed { i, row ->
                    Row {
                        row.forEachIndexed { j, cell ->
                            TextField(
                                value = input[i][j],
                                onValueChange = {
                                    if (cell == null && it.length <= 1 && (it == "" || it.toIntOrNull() != null)) {
                                        input[i][j] = it
                                        viewModel.saveInput(input.map { r -> r.toList() })
                                    }
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(1.dp)
                                    .border(1.dp, Color.Gray)
                                    .background(Color.White),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                enabled = cell == null
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de acciones
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = {
                        val correct = input.mapIndexed { i, row ->
                            row.mapIndexed { j, value ->
                                value.toIntOrNull() == state.solution[i][j]
                            }
                        }.flatten().all { it }

                        message = if (correct) "¡Correcto! Puzzle completo 🎉" else "Hay errores en tu solución ❌"
                    }) {
                        Text("Verificar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        input = state.puzzle.map { row -> row.map { it?.toString() ?: "" }.toMutableStateList() }.toMutableStateList()
                        viewModel.saveInput(input.map { r -> r.toList() })
                        message = null
                    }) {
                        Text("Reiniciar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        viewModel.loadSudoku()
                        message = null
                    }) {
                        Text("Nuevo")
                    }
                }

                message?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
