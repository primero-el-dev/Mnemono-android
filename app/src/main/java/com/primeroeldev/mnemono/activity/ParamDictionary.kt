package com.primeroeldev.mnemono.activity

sealed class ParamDictionary
{
    companion object
    {
        const val GAME_ID_KEY = "gameId"
        const val CHECKED_ANSWERS_KEY = "checkedAnswers"
        const val CORRECT_ANSWERS_COUNT_KEY = "correctAnswersCount"
        const val CORRECT_ANSWERS_KEY = "correctAnswers"
        const val REAL_GAME_DURATION_KEY = "realGameDuration"
    }
}
