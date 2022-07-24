package com.primeroeldev.mnemono.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.game.GameRepository
import com.primeroeldev.mnemono.general.TimeUtil

class GameResultActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        val gameId = this.intent.getLongExtra(ParamDictionary.GAME_ID_KEY, 0)
        val game = GameRepository(applicationContext, null).find(gameId) as? Game
        if (game == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val checkedAnswers = this.intent.getStringExtra(ParamDictionary.CHECKED_ANSWERS_KEY)
        val time = TimeUtil.longToTimeString(game?.durationInSeconds!!.toLong())

        (findViewById(R.id.game_result_checked_answers) as TextView).text = Html.fromHtml(checkedAnswers)
        (findViewById(R.id.game_result_correct_count) as TextView).text = "${game.correctAnswersCount} / ${game.allAnswersCount} in ${time}"
        (findViewById(R.id.game_result_created_at) as TextView).text = "On " + game.createdAt
    }

    fun goBack(view: View): Unit
    {
        finish()
    }
}