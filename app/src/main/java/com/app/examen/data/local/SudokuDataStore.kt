package com.app.examen.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "sudoku_store")

class SudokuDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val puzzleKey = stringPreferencesKey("puzzle")
    private val solutionKey = stringPreferencesKey("solution")
    private val inputKey = stringPreferencesKey("input")

    suspend fun savePuzzle(puzzle: List<List<Int?>>) {
        val json = Gson().toJson(puzzle)
        context.dataStore.edit { it[puzzleKey] = json }
    }

    suspend fun saveSolution(solution: List<List<Int>>) {
        val json = Gson().toJson(solution)
        context.dataStore.edit { it[solutionKey] = json }
    }

    suspend fun saveInput(input: List<List<String>>) {
        val json = Gson().toJson(input)
        context.dataStore.edit { it[inputKey] = json }
    }

    suspend fun loadPuzzle(): List<List<Int?>>? {
        val json = context.dataStore.data.first()[puzzleKey] ?: return null
        val type = object : TypeToken<List<List<Int?>>>() {}.type
        return Gson().fromJson(json, type)
    }

    suspend fun loadSolution(): List<List<Int>>? {
        val json = context.dataStore.data.first()[solutionKey] ?: return null
        val type = object : TypeToken<List<List<Int>>>() {}.type
        return Gson().fromJson(json, type)
    }

    suspend fun loadInput(): List<List<String>>? {
        val json = context.dataStore.data.first()[inputKey] ?: return null
        val type = object : TypeToken<List<List<String>>>() {}.type
        return Gson().fromJson(json, type)
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}