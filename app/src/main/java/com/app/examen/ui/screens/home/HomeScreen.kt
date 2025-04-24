package com.app.examen.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.examen.viewmodel.SudokuUiState
import com.app.examen.viewmodel.SudokuViewModel

@Composable
fun HomeScreen(viewModel: SudokuViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSavedGame()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sudoku App", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is SudokuUiState.Loading -> {
                CircularProgressIndicator()
            }

            is SudokuUiState.Error -> {
                Text("Error: ${(state as SudokuUiState.Error).message}")
            }

            is SudokuUiState.Success -> {
                val puzzle = state.puzzle
                val solution = state.solution
                val inputGrid = remember { mutableStateListOf<MutableList<String>>() }
                var isCorrect by remember { mutableStateOf<Boolean?>(null) }

                LaunchedEffect(puzzle) {
                    inputGrid.clear()
                    inputGrid.addAll(puzzle.map { row ->
                        row.map { it?.toString() ?: "" }.toMutableList()
                    })
                    isCorrect = null
                }

                Column {
                    puzzle.forEachIndexed { rowIndex, row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            row.forEachIndexed { colIndex, cell ->
                                val value = inputGrid[rowIndex][colIndex]
                                if (cell == null) {
                                    OutlinedTextField(
                                        value = value,
                                        onValueChange = {
                                            if (it.length <= 1 && (it.isEmpty() || it.toIntOrNull() in 1..9)) {
                                                inputGrid[rowIndex][colIndex] = it
                                                viewModel.saveInput(inputGrid)
                                            }
                                        },
                                        modifier = Modifier
                                            .width(40.dp)
                                            .padding(2.dp),
                                        singleLine = true
                                    )

                                } else {
                                    Text(
                                        text = cell.toString(),
                                        modifier = Modifier
                                            .width(40.dp)
                                            .padding(8.dp),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = {
                            isCorrect = inputGrid.withIndex().all { (i, row) ->
                                row.withIndex().all { (j, value) ->
                                    value.toIntOrNull() == solution[i][j]
                                }
                            }
                        }) {
                            Text("Verificar solución")
                        }

                        Button(onClick = {
                            viewModel.loadSudoku()
                        }) {
                            Text("Nuevo Sudoku")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    when (isCorrect) {
                        true -> Text("✅ ¡Solución correcta!", fontWeight = FontWeight.Bold)
                        false -> Text("❌ Hay errores, revisa tu tablero", fontWeight = FontWeight.Bold)
                        null -> {}
                    }
                }
            }
        }
    }
}
