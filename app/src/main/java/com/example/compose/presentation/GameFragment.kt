package com.example.compose.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.compose.databinding.FragmentGameBinding
import com.example.compose.domain.entity.GameResult
import com.example.compose.domain.entity.Level

class GameFragment : Fragment() {
    private val args by navArgs<GameFragmentArgs>()

    private val viewModelFactory: GameViewModelFactory by lazy {
        GameViewModelFactory(
            args.level,
            requireActivity().application
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("Fragment == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setClickListenersToOptions()
    }

    private fun setClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                viewModel.getAnswer(tvOption.text.toString().toInt())
            }
        }
    }


    private fun launchFinishedFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }

    companion object {
        const val NAME = "GameFragment"
        const val KEY_LEVEL = "level"
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            question.observe(viewLifecycleOwner) {
                binding.tvSum.text = it.sum.toString()
                binding.tvLeftNumber.text = it.visibleNumber.toString()
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = it.option[i].toString()
                }
            }
            percentOfRightAnswer.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it, true)
            }
            enoughCountOfRightAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.setTextColor(setColor(it))
            }
            progressAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.text = it
            }
            enoughPercentOfRightAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.progressTintList = ColorStateList.valueOf(setColor(it))
            }
            formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }
            minPercent.observe(viewLifecycleOwner) {
                binding.progressBar.secondaryProgress = it
            }
            gameResult.observe(viewLifecycleOwner) {
                launchFinishedFragment(it)
            }
        }
    }

    private fun setColor(state: Boolean): Int {
        val colorResId = if (state) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}