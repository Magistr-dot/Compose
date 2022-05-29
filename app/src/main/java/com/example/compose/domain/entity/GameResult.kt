package com.example.compose.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult(
    val win: Boolean,
    val countRightAnswer: Int,
    val countOfQuestion: Int,
    val gameSettings: GameSettings
) : Parcelable