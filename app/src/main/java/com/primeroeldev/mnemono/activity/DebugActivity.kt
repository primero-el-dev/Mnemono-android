package com.primeroeldev.mnemono.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.game.GameRepository
import com.primeroeldev.mnemono.general.EntityInterface

class DebugActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        val gameRepository = GameRepository(applicationContext, null)
        val count = gameRepository.count()
//        val game = gameRepository.find(1) as? Game
//        val game = gameRepository.findAll()[2] as? Game


//        var game: Game? = Game()K
//        game?.type = Game.CARDS_TYPE
//        game?.status = Game.NOT_STARTED_STATUS
//        game?.correctAnswersCount = 2
//        game?.allAnswersCount = 10
//        game?.includedInStatistics = 1
//        game?.durationInSeconds = 123
//        game?.createdAt = "2022-07-22"
//
//        val gameId = GameRepository(applicationContext, null).insert(game!! as EntityInterface)
//
//        val list: ArrayList<Pair<String, Any?>> = ArrayList()
//        list.add(Pair("type", Game.WORDS_TYPE as Any?))
//
//        val deleteArgs: Array<String> = arrayOf(Game.CARDS_TYPE)
//        val deleted = GameRepository(applicationContext, null).updateBy(game, "type = ?", deleteArgs)
//
//        game = gameRepository.findOneBy(list) as? Game
//
//        if (game != null) {
//            (findViewById(R.id.debug_text) as TextView).text = game._id.toString()
////            (findViewById(R.id.debug_text) as TextView).text = gameId.toString()
////            (findViewById(R.id.debug_text) as TextView).text = game.type
//        } else {
//            (findViewById(R.id.debug_text) as TextView).text = "NONE FOUND"
//        }
//        (findViewById(R.id.debug_text) as TextView).text = deleted.toString()
    }
}