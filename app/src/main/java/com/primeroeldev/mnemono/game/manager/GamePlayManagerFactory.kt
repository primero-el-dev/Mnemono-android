package com.primeroeldev.mnemono.game.manager

import com.primeroeldev.mnemono.game.Game

class GamePlayManagerFactory
{
    companion object
    {
        /**
         * @throws Exception
         */
        fun dispatch(game: Game): GamePlayManager
        {
            return when (game.type) {
                Game.NUMBERS_TYPE -> NumbersGamePlayManager()
                else -> throw Exception("Wrong game type ${game.type}")
            }
        }
    }
}