package com.primeroeldev.mnemono.game.manager

import com.primeroeldev.mnemono.general.toInt
import kotlin.random.Random

class NumbersGamePlayManager : GamePlayManager
{
    override fun generateAnswers(count: Int): String
    {
        var result = ""
        var i = 0
        while (i < count) {
            result += Random.nextInt(10).toString()
            i++
        }

        return result
    }

    override fun presentAnswers(answers: String): String
    {
        return answers.chunked(10).joinToString(" ")
    }

    override fun checkAnswers(correct: String, provided: String): Pair<String, Int>
    {
        val providedFormatted = provided.replace("(\\s|[^\\d])+".toRegex(), "")
        val correctFormatted = correct.replace("(\\s|[^\\d])+".toRegex(), "")
        var answers = ""
        var correctCount = 0
        var index = 0
        val limit = minOf(providedFormatted.length, correctFormatted.length)

        while (index < limit) {
            answers += if (index % 10 == 0) " " else ""

            val providedChar = providedFormatted[index]
            val isCorrect = providedChar == correctFormatted[index]
            if (isCorrect) {
                answers += providedChar
            }
            else {
                answers += "<font color=\"red\">$providedChar</font>"
            }
            correctCount += isCorrect.toInt()
            index++
        }

        return Pair(answers, correctCount)
    }

    override fun getAnswerInputHint(): String
    {
        return "All spaces will be trimmed"
    }
}