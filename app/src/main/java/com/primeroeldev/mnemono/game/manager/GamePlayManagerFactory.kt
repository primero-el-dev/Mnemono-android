package com.primeroeldev.mnemono.game.manager

import android.content.Context
import com.primeroeldev.mnemono.entity.Game

class GamePlayManagerFactory
{
    companion object
    {
        /**
         * @throws Exception
         */
        fun dispatch(game: Game, context: Context): GamePlayManager
        {
            return when (game.type) {
                Game.WORDS_TYPE -> WordsGamePlayManager(context)
                Game.DIGITS_TYPE -> DigitsGamePlayManager()
                Game.CARDS_TYPE -> CardsGamePlayManager(context.resources)
                else -> throw Exception("Wrong game type ${game.type}")
            }
        }
    }
}