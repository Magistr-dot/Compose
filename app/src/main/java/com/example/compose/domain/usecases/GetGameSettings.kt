package com.example.compose.domain.usecases

import com.example.compose.domain.entity.GameSettings
import com.example.compose.domain.entity.Level
import com.example.compose.domain.repository.GameRepository

class GetGameSettings(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}