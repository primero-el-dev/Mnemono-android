package com.primeroeldev.mnemono.activity


sealed class ParamDictionary
{
    companion object
    {
        const val GAME_ID_KEY = "gameId"
        const val CHECKED_ANSWERS_KEY = "checkedAnswers"
        const val CORRECT_ANSWERS_KEY = "correctAnswers"
    }
}
