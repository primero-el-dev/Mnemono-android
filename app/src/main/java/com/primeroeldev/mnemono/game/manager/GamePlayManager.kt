package com.primeroeldev.mnemono.game.manager

interface GamePlayManager
{
    fun generateAnswers(count: Int): String

    fun presentAnswers(answers: String): String

    fun checkAnswers(correct: String, provided: String): Pair<String, Int>

    fun getAnswerInputHint(): String
}