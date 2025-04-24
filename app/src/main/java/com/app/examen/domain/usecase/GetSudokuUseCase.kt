package com.app.examen.domain.usecase

import com.app.examen.data.model.SudokuDto
import com.app.examen.data.repository.SudokuRepository
import javax.inject.Inject

class GetSudokuUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(
        width: Int = 9,
        height: Int = 9,
        difficulty: String = "easy"
    ): SudokuDto {
        return repository.getSudoku(width, height, difficulty)
    }
}