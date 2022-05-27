package com.example.compose.domain.entity

data class GameResult(
    val win: Boolean,
    val countRightAnswer: Int,
    val countOfQuestion: Int,
    val gameSettings: GameSettings
) {
}