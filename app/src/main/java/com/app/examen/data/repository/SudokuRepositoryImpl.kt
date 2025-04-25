package com.app.examen.data.repository

import com.app.examen.data.model.SudokuDto
import com.app.examen.domain.repository.SudokuRepository
import com.app.examen.network.SudokuApi
import javax.inject.Inject

class SudokuRepositoryImpl @Inject constructor(
    private val api: SudokuApi
) : SudokuRepository {

    override suspend fun getSudoku(width: Int, height: Int, difficulty: String): SudokuDto {
        return api.getSudoku(width, height, difficulty)
    }
}
