package com.example.compose.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.compose.R
import com.example.compose.databinding.FragmentGameFinishedBinding
import com.example.compose.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("Fragment == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()
        bindViews()
    }

    private fun bindViews() {
        with(binding) {
            result.setImageResource(getResId())
            tvRequiredAnswers.text = String.format(
                getString(R.string.you_need_right_answers),
                gameResult.gameSettings.minCountRightAnswer
            )
            tvScoreAnswers.text = String.format(
                getString(R.string.you_score),
                gameResult.countRightAnswer
            )
            tvRequiredPercentage.text = String.format(
                getString(R.string.need_percent),
                gameResult.gameSettings.minPercentRightAnswer
            )
            tvScorePercentage.text = String.format(
                getString(R.string.you_percent),
                calculatePercentOfRightAnswers()
            )
        }
    }

    private fun calculatePercentOfRightAnswers() = with(gameResult) {
        if (countOfQuestion == 0){
            0
        } else {
            ((countRightAnswer / countOfQuestion.toDouble()) * 100).toInt()
        }
    }
    private fun getResId(): Int {
        return if (gameResult.win) {
            R.drawable.good_result
        } else {
            R.drawable.bad_result
        }

    }

    private fun setupClickListener() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )


    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(GAME_RESULT)?.let {
            gameResult = it
        }
    }

    companion object {
        private const val GAME_RESULT = "game result"
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_RESULT, gameResult)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}