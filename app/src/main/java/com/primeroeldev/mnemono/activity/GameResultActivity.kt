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

        val checkedAnswers = this.intent.getStringExtra(ParamDictionary.CHECKED_ANSWERS_KEY) ?: ""

        this.prepareDisplay(checkedAnswers, game!!)
    }

    fun goBack(view: View): Unit
    {
        finish()
    }

    private fun prepareDisplay(checkedAnswers: String, game: Game): Unit
    {
        val time = TimeUtil.longToTimeString(game.durationInSeconds.toLong())

        findViewById<TextView>(R.id.game_result_checked_answers).text = Html.fromHtml(checkedAnswers)
        findViewById<TextView>(R.id.game_result_correct_count).text = "${game.correctAnswersCount} / ${game.allAnswersCount} in ${time}"
        findViewById<TextView>(R.id.game_result_type).text = "Game type: " + game.type
        findViewById<TextView>(R.id.game_result_status).text = "Game status: " + game.status
        findViewById<TextView>(R.id.game_result_included_in_statistics).text = "Included in statistics: " + if (game.includedInStatistics == 1) "yes" else "no"
        findViewById<TextView>(R.id.game_result_created_at).text = game.createdAt
    }
}