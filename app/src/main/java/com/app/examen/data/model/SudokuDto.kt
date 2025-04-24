package com.app.examen.data.model

data class SudokuDto(
    val puzzle: List<List<Int?>>,
    val solution: List<List<Int>>
)