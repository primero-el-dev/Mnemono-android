package com.primeroeldev.mnemono.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.game.GameRepository
import com.primeroeldev.mnemono.general.TimeUtil

class GamePlayActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        val gameId = this.intent?.getLongExtra("gameId", 0)!!
        val game = GameRepository(applicationContext, null).find(gameId) as? Game

        if (gameId == 0L) {
            val intent = Intent(this, GameStartActivity::class.java)
            startActivity(intent)
        }

        val timerView = findViewById(R.id.game_play_timer) as TextView
//        val timer = this.initTimer(game!!, timerView)
//        timer.start()
    }

    private fun initTimer(game: Game, timerView: TextView): CountDownTimer
    {
        return object: CountDownTimer(game.durationInSeconds.toLong() * 1000, 1000L)
        {
            override fun onTick(millisUntilFinished: Long): Unit
            {
                timerView.text = TimeUtil.longToTimeString(millisUntilFinished)
            }

            override fun onFinish(): Unit
            {
                timerView.text = TimeUtil.longToTimeString(0)
            }
        }
    }
}