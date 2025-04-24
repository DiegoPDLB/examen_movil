package com.app.examen.domain.respository

import com.app.examen.data.model.SudokuDto

interface SudokuRepository {
    suspend fun getSudoku(width: Int, height: Int, difficulty: String): SudokuDto
}
