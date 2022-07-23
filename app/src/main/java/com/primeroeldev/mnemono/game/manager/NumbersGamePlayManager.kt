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

    override fun checkAnswers(generated: String, provided: String): Pair<String, Int>
    {
        val providedFormatted = provided.replace("\\s+".toRegex(), "")
        val generatedFormatted = provided.replace("\\s+".toRegex(), "")
        var answers = ""
        var correctCount = 0
        var index = 1

        while (index <= providedFormatted.length) {
            val correct = providedFormatted[index] == generatedFormatted[index]
            if (correct) {
                answers += "<font color=\"green\">" + providedFormatted[index] + "</font>"
            }
            else {
                answers += "<font color=\"red\">" + providedFormatted[index] + "</font>"
            }
            answers += if (index % 10 == 0) " " else ""
            correctCount += correct.toInt()
            index++
        }

        return Pair(answers, correctCount)
    }
}