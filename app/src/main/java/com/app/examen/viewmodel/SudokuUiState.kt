package com.app.examen.viewmodel

sealed class SudokuUiState {
    object Loading : SudokuUiState()
    data class Success(
        val puzzle: List<List<Int?>>,
        val solution: List<List<Int>>
    ) : SudokuUiState()
    data class Error(val message: String) : SudokuUiState()
}
