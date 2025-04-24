package com.app.examen.network

import com.app.examen.data.model.SudokuDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SudokuApi {
    @GET("sudokugenerate")
    suspend fun getSudoku(
        @Query("width") width: Int,
        @Query("height") height: Int,
        @Query("difficulty") difficulty: String
    ): SudokuDto
}