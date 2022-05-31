package com.example.compose.domain.usecases

import com.example.compose.domain.entity.Question
import com.example.compose.domain.repository.GameRepository

class GenerateQuestions(
    private val repository: GameRepository
) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTION)
    }

    private companion object {
        private const val COUNT_OF_OPTION = 6
    }
}