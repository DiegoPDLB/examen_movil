package com.app.examen.ui.screens.puzzle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.examen.ui.components.SudokuGrid
import com.app.examen.viewmodel.SudokuViewModel
import com.app.examen.viewmodel.state.SudokuUiState
import kotlinx.coroutines.launch

@Composable
fun PuzzleScreen(viewModel: SudokuViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (val state = uiState) {
            is SudokuUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SudokuUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.message}")
                }
            }

            is SudokuUiState.Success -> {
                val puzzle = state.puzzle
                val solution = state.solution
                var userInput by remember { mutableStateOf(state.input) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SudokuGrid(
                        puzzle = puzzle,
                        userInput = userInput,
                        onCellChange = { row, col, value ->
                            userInput = userInput.toMutableList().also {
                                it[row] = it[row].toMutableList().also { r -> r[col] = value }
                            }
                        }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            val isCorrect = userInput.mapIndexed { i, row ->
                                row.mapIndexed { j, cell ->
                                    cell == solution[i][j].toString()
                                }.all { it }
                            }.all { it }

                            coroutineScope.launch {
                                if (isCorrect) {
                                    viewModel.saveInput(userInput)
                                    snackbarHostState.showSnackbar("🎉 Sudoku completado correctamente")
                                } else {
                                    snackbarHostState.showSnackbar("❌ Hay errores en el Sudoku")
                                }
                            }
                        }) {
                            Text("Verificar")
                        }

                        Button(onClick = {
                            userInput = puzzle.map { row -> row.map { it?.toString() ?: "" } }
                        }) {
                            Text("Reiniciar")
                        }

                        Button(onClick = {
                            viewModel.loadSudoku()
                        }) {
                            Text("Nuevo Sudoku")
                        }
                    }
                }
            }
        }
    }
}
