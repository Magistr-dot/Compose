package com.example.compose.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val maxSumValve: Int,
    val minCountRightAnswer: Int,
    val minPercentRightAnswer: Int,
    val gameTimeInSeconds: Int
) : Parcelable