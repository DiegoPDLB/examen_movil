package com.app.examen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.examen.data.local.SudokuDataStore
import com.app.examen.domain.usecase.GetSudokuUseCase
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

            if (savedPuzzle != null && savedSolution != null) {
                _uiState.value = SudokuUiState.Success(
                    puzzle = savedPuzzle,
                    solution = savedSolution
                )
            } else {
                loadSudoku()
            }
        }
    }

    fun loadSudoku(width: Int = 9, height: Int = 9, difficulty: String = "easy") {
        viewModelScope.launch {
            _uiState.value = SudokuUiState.Loading
            try {
                val result = getSudokuUseCase(width, height, difficulty)
                _uiState.value = SudokuUiState.Success(result.puzzle, result.solution)

                // Guardar puzzle, solución e input inicial
                dataStore.savePuzzle(result.puzzle)
                dataStore.saveSolution(result.solution)
                dataStore.saveInput(result.puzzle.map { row ->
                    row.map { it?.toString() ?: "" }
                })

            } catch (e: Exception) {
                _uiState.value = SudokuUiState.Error(
                    message = e.localizedMessage ?: "Error desconocido"
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
