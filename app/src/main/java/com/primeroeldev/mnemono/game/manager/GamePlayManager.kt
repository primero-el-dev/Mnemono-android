package com.primeroeldev.mnemono.game.manager

import android.graphics.Bitmap


interface GamePlayManager
{
    companion object
    {
        const val TEXT_INPUT_TYPE = "text"
        const val IMAGE_INPUT_TYPE = "image"
    }

    fun generateAnswers(count: Int): String

    fun presentAnswersText(answers: String): String
    {
        return ""
    }

    fun presentAnswersImage(answers: String): Bitmap
    {
        return Bitmap.createBitmap(0, 0, Bitmap.Config.RGB_565)
    }

    /**
     * Returns [answerHtmlString, correctAnswersCount]
     */
    fun checkAnswers(correct: String, provided: String): Pair<String, Int>

    fun getAnswerInputHint(): String

    fun getInputType(): String
    {
        return TEXT_INPUT_TYPE
    }
}