package com.example.compose.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.compose.R
import com.example.compose.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()
        bindViews()
    }

    private fun bindViews() {
        with(binding) {
            result.setImageResource(getResId())
            with(args) {
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
    }

    private fun calculatePercentOfRightAnswers() = with(args.gameResult) {
        if (countOfQuestion == 0) {
            0
        } else {
            ((countRightAnswer / countOfQuestion.toDouble()) * 100).toInt()
        }
    }

    private fun getResId(): Int {
        return if (args.gameResult.win) {
            R.drawable.good_result
        } else {
            R.drawable.bad_result
        }
    }

    private fun setupClickListener() {
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}