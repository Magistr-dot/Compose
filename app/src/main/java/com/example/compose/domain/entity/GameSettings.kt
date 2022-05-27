package com.example.compose.domain.entity

data class GameSettings(
    val maxSumValve: Int,
    val minCountRightAnswer: Int,
    val minPercentRightAnswer: Int,
    val gameTimeInSeconds: Int
) {
}