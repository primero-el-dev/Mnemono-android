package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.game.GameRepository
import com.primeroeldev.mnemono.game.manager.GamePlayManager
import com.primeroeldev.mnemono.game.manager.GamePlayManagerFactory
import com.primeroeldev.mnemono.general.TimeUtil

class GamePlayActivity : AppCompatActivity()
{
    private lateinit var gameManager: GamePlayManager
    private lateinit var answers: String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        val gameId = this.intent?.getLongExtra(ParamDictionary.GAME_ID_KEY, 0)!!
        val game = GameRepository(applicationContext, null).find(gameId) as? Game

        if (gameId == 0L || game == null) {
            val intent = Intent(this, GameStartActivity::class.java)
            startActivity(intent)
        }

        val answersView = findViewById(R.id.game_play_correct_answers) as TextView
        this.gameManager = GamePlayManagerFactory.dispatch(game!!)
        this.answers = this.gameManager.generateAnswers(game.allAnswersCount)
        answersView.text = this.gameManager.presentAnswers(this.answers)
//        answersView.setMovementMethod(newScrollingMovementMethod())

        val timerView = findViewById(R.id.game_play_timer) as TextView
        val timer = this.initTimer(game, timerView)
        timer.start()
    }

    fun processToAnswer(view: View): Unit
    {
        val intent = Intent(this, GameAnswerActivity::class.java)
        intent.putExtra(ParamDictionary.CORRECT_ANSWERS_KEY, this.answers)
        startActivity(intent)
    }

    private fun initTimer(game: Game, timerView: TextView): CountDownTimer
    {
        return object: CountDownTimer(game.durationInSeconds.toLong() * 1000, 1000L)
        {
            override fun onTick(millisUntilFinished: Long): Unit
            {
                if (millisUntilFinished < 30000) {
                    timerView.setTextColor(Color.RED)
                }
                timerView.text = TimeUtil.longToTimeString(millisUntilFinished)
            }

            override fun onFinish(): Unit
            {
                timerView.text = TimeUtil.longToTimeString(0)
            }
        }
    }
}