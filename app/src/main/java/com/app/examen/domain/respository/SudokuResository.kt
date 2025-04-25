// Archivo: com/app/examen/domain/repository/SudokuRepository.kt

package com.app.examen.domain.repository

import com.app.examen.data.model.SudokuDto

interface SudokuRepository {
    suspend fun getSudoku(width: Int, height: Int, difficulty: String): SudokuDto
}
