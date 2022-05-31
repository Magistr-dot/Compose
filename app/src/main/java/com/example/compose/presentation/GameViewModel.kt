package com.example.compose.presentation

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.compose.R
import com.example.compose.data.GameRepositoryImpl
import com.example.compose.domain.entity.GameResult
import com.example.compose.domain.entity.GameSettings
import com.example.compose.domain.entity.Level
import com.example.compose.domain.entity.Question
import com.example.compose.domain.usecases.GenerateQuestions
import com.example.compose.domain.usecases.GetGameSettings

class GameViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private val repository = GameRepositoryImpl

    private lateinit var gameSettings: GameSettings


    private val generateQuestionsUseCase = GenerateQuestions(repository)
    private val getGameSettingsUseCase = GetGameSettings(repository)

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswer = MutableLiveData<Int>()
    val percentOfRightAnswer: LiveData<Int>
        get() = _percentOfRightAnswer

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers

    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswers = 0
    private var countOfQuestion = 0

    init {
        startGame()
    }

    private fun startGame() {
        gameSettings()
        generateQuestion()
        startTimer()
        updateProgress()
    }

    fun getAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswer.value = percent
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.percent),
            countOfRightAnswers,
            gameSettings.minCountRightAnswer
        )
        _enoughCountOfRightAnswers.value = countOfRightAnswers >= gameSettings.minCountRightAnswer
        _enoughPercentOfRightAnswers.value = percent >= gameSettings.minPercentRightAnswer
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return if (countOfRightAnswers == 0) {
            0
        } else {
            ((countOfRightAnswers / countOfQuestion.toDouble()) * 100).toInt()
        }
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestion++
    }

    private fun gameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentRightAnswer
    }

    private fun startTimer() {
        val timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(p0: Long) {
                _formattedTime.value = formatTime(p0)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer.start()

    }

    private fun generateQuestion() {
        _question.value = generateQuestionsUseCase(gameSettings.maxSumValve)
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTE
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTE)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            win = enoughCountOfRightAnswers.value == true && enoughPercentOfRightAnswers.value == true,
            countRightAnswer = countOfRightAnswers,
            countOfQuestion = countOfQuestion,
            gameSettings = gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTE = 60
    }
}