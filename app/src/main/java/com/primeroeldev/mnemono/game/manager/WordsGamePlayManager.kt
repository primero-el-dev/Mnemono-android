package com.primeroeldev.mnemono.game.manager

import android.content.Context
import com.primeroeldev.mnemono.entity.Word
import com.primeroeldev.mnemono.general.toInt
import com.primeroeldev.mnemono.repository.WordRepository


class WordsGamePlayManager(private val context: Context) : GamePlayManager
{
    override fun generateAnswers(count: Int): String
    {
        return WordRepository(this.context, null)
            .findRandom(count)
            .map { (it as Word).name }
            .joinToString(",\n")
    }

    override fun presentAnswers(answers: String): String
    {
        return answers
    }

    override fun checkAnswers(correct: String, provided: String): Pair<String, Int>
    {
        val providedWords = provided.replace("(\\s)+".toRegex(), "").split(",")
        val correctWords = correct.replace("(\\s)+".toRegex(), "").split(",")
        var answers = ""
        var correctCount = 0
        var index = 0
        val limit = minOf(providedWords.size, correctWords.size)

        while (index < limit) {
            val providedWord = providedWords[index]
            val isCorrect = providedWord == correctWords[index]
            if (isCorrect) {
                answers += "<font color=\"black\">$providedWord</font>,\n"
            }
            else {
                answers += "<font color=\"red\">$providedWord</font>,\n"
            }
            correctCount += isCorrect.toInt()
            index++
        }

        return Pair(answers, correctCount)
    }

    override fun getAnswerInputHint(): String
    {
        return "All white chars will be trimmed.\nDivide words with commas."
    }
}