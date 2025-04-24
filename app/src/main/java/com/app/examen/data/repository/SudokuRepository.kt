package com.app.examen.data.repository

import com.app.examen.data.model.SudokuDto
import com.app.examen.network.SudokuApi
import javax.inject.Inject

class SudokuRepository @Inject constructor(
    private val api: SudokuApi
) {
    suspend fun getSudoku(width: Int, height: Int, difficulty: String): SudokuDto {
        return api.getSudoku(width, height, difficulty)
    }
}