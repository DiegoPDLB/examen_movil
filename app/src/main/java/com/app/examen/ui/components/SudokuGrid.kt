package com.app.examen.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SudokuGrid(
    puzzle: List<List<Int?>>,
    userInput: List<List<String>>,
    onCellChange: (row: Int, col: Int, value: String) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp)
    ) {
        puzzle.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, cell ->
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .border(1.dp, Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (cell == null) {
                            OutlinedTextField(
                                value = userInput[rowIndex][colIndex],
                                onValueChange = { input ->
                                    if (input.length <= 1 && (input.isBlank() || input.matches(Regex("[1-9]")))) {
                                        onCellChange(rowIndex, colIndex, input)
                                    }
                                },
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(1.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    disabledBorderColor = Color.Transparent,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    disabledContainerColor = Color.White
                                )
                            )
                        } else {
                            Text(
                                text = cell.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}
