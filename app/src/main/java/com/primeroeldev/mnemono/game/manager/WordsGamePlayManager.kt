package com.primeroeldev.mnemono.game.manager

import android.content.Context
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.general.toInt
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList
import kotlin.random.Random


class WordsGamePlayManager(private val context: Context) : GamePlayManager
{
    override fun generateAnswers(count: Int): String
    {
        val file = BufferedReader(InputStreamReader(this.context.resources.openRawResource(R.raw.words)))
        val words: ArrayList<String> = ArrayList()

        while (true) {
            val line = file.readLine() ?: break
            words.add(line)
        }
        val length = words.size

        val choosen: ArrayList<String> = ArrayList()
        var i = 0
        while (i < count) {
            choosen.add(words[Random.nextInt(length)])
            i++
        }

        return choosen.joinToString(",\n")
    }

    override fun presentAnswersText(answers: String): String
    {
        return answers
    }

    override fun checkAnswers(correct: String, provided: String): Pair<String, Int>
    {
        val providedWords = provided.replace("(\\s|)+".toRegex(), "").split(",")
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
        return "Divide words with commas. " +
            "All white chars will be trimmed."
    }
}