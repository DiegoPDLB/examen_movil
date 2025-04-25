package com.app.examen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.examen.data.local.SudokuDataStore
import com.app.examen.domain.usecase.GetSudokuUseCase
import com.app.examen.viewmodel.state.SudokuUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SudokuViewModel @Inject constructor(
    private val getSudokuUseCase: GetSudokuUseCase,
    private val dataStore: SudokuDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow<SudokuUiState>(SudokuUiState.Loading)
    val uiState: StateFlow<SudokuUiState> = _uiState

    fun loadSavedGame() {
        viewModelScope.launch {
            val savedPuzzle = dataStore.loadPuzzle()
            val savedSolution = dataStore.loadSolution()
            val savedInput = dataStore.loadInput()

            if (savedPuzzle != null && savedSolution != null && savedInput != null) {
                _uiState.value = SudokuUiState.Success(
                    puzzle = savedPuzzle,
                    solution = savedSolution,
                    input = savedInput
                )
            } else {
                loadSudoku()
            }
        }
    }

    fun loadSudoku(width: Int = 9, height: Int = 9, difficulty: String = "easy") {
        viewModelScope.launch {
            _uiState.value = SudokuUiState.Loading

            val safeDifficulty = difficulty.lowercase().trim()
            val allowedDifficulties = listOf("easy", "medium", "hard")

            if (safeDifficulty !in allowedDifficulties) {
                _uiState.value = SudokuUiState.Error("Dificultad inválida: '$difficulty'. Usa: easy, medium o hard.")
                return@launch
            }

            try {
                Log.d("SUDOKU_API", "Cargando puzzle con width=$width, height=$height, difficulty=$safeDifficulty")
                val result = getSudokuUseCase(width, height, safeDifficulty)
                val input = result.puzzle.map { row -> row.map { it?.toString() ?: "" } }

                _uiState.value = SudokuUiState.Success(
                    puzzle = result.puzzle,
                    solution = result.solution,
                    input = input
                )

                dataStore.savePuzzle(result.puzzle)
                dataStore.saveSolution(result.solution)
                dataStore.saveInput(input)
            } catch (e: Exception) {
                _uiState.value = SudokuUiState.Error(
                    message = e.localizedMessage ?: "Error desconocido al cargar el Sudoku"
                )
            }
        }
    }

    fun saveInput(input: List<List<String>>) {
        viewModelScope.launch {
            dataStore.saveInput(input)
        }
    }
}