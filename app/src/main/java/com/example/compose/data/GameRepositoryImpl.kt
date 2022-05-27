package com.example.compose.data

import com.example.compose.domain.entity.GameSettings
import com.example.compose.domain.entity.Level
import com.example.compose.domain.entity.Question
import com.example.compose.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min

import kotlin.random.Random

object GameRepositoryImpl : GameRepository {
    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER = 1
    private const val ONE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + ONE)
        val visibleNumber = Random.nextInt(MIN_ANSWER, sum)
        val rightNumber = sum - visibleNumber
        val option = HashSet<Int>()
        option.add(rightNumber)
        val from = max(rightNumber - countOfOptions, MIN_ANSWER)
        val to = min(maxSumValue, rightNumber + countOfOptions)
        while (option.size < countOfOptions) {
            option.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNumber, option.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> {
                GameSettings(
                    10,
                    3,
                    50,
                    8
                )
            }
            Level.EASY -> {
                GameSettings(
                    10,
                    10,
                    70,
                    60
                )
            }
            Level.NORMAL -> {
                GameSettings(
                    25,
                    25,
                    80,
                    40
                )
            }
            Level.HARD -> {
                GameSettings(
                    50,
                    30,
                    100,
                    35
                )
            }
        }
    }
}