package com.app.examen.viewmodel.state

sealed class SudokuUiState {
    object Loading : SudokuUiState()

    data class Success(
        val puzzle: List<List<Int?>>,
        val solution: List<List<Int>>,
        val input: List<List<String>>
    ) : SudokuUiState()

    data class Error(val message: String) : SudokuUiState()
}