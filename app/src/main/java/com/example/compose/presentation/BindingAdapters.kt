package com.example.compose.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.compose.R
import com.example.compose.domain.entity.GameResult

interface OnOptionClick {
    fun onOption(option: Int)
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.you_need_right_answers),
        count
    )
}

@BindingAdapter("requiredPercent")
fun bindRequiredPercent(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.need_percent),
        count
    )
}

@BindingAdapter("youAnswer")
fun bindYouAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.you_score),
        count
    )

}

@BindingAdapter("youPercent")
fun bindYouPercent(textView: TextView, gameResult: GameResult) {

    textView.text = String.format(
        textView.context.getString(R.string.you_percent),
        calculatePercentOfRightAnswers(gameResult)
    )
}

private fun calculatePercentOfRightAnswers(gameResult: GameResult) = with(gameResult) {
    if (countOfQuestion == 0) {
        0
    } else {
        ((countRightAnswer / countOfQuestion.toDouble()) * 100).toInt()
    }
}

@BindingAdapter("result")
fun bindResult(imageView: ImageView, win: Boolean) {
    imageView.setImageResource(getResId(win))
}

private fun getResId(win: Boolean): Int {
    return if (win) {
        R.drawable.good_result
    } else {
        R.drawable.bad_result
    }
}

@BindingAdapter("setEnoughCount")
fun bindEnoughCount(textView: TextView, enough: Boolean) {
    textView.setTextColor(setColor(textView.context, enough))
}

@BindingAdapter("setEnoughProgressBar")
fun bindEnoughProgressBar(progressBar: ProgressBar, enough: Boolean) {
    progressBar.progressTintList = ColorStateList.valueOf(setColor(progressBar.context, enough))
}

private fun setColor(context: Context, state: Boolean): Int {
    val colorResId = if (state) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClick(textView: TextView, clickListener: OnOptionClick) {
    textView.setOnClickListener {
        clickListener.onOption(textView.text.toString().toInt())
    }
}