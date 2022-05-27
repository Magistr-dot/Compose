package com.example.compose.domain.repository

import com.example.compose.domain.entity.GameSettings
import com.example.compose.domain.entity.Level
import com.example.compose.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue:Int,
        countOfOptions:Int
    ): Question
    fun getGameSettings(level: Level): GameSettings

}