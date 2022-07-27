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
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

class GamePlayActivity : AppCompatActivity()
{
    private lateinit var gameManager: GamePlayManager
    private lateinit var answers: String
    private lateinit var game: Game
    private lateinit var timer: CountDownTimer
    private var timeStarted: Long = 0
    private var gameId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?): Unit
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        val gameRepository = GameRepository(applicationContext, null)
        val gameId = this.intent?.getLongExtra(ParamDictionary.GAME_ID_KEY, 0)!!
        val game = gameRepository.find(gameId) as? Game

        if (gameId == 0L || game == null) {
            val intent = Intent(this, GameStartActivity::class.java)
            startActivity(intent)
        }

        this.game = game!!

        this.game.status = Game.JUST_STARTED_STATUS
        gameRepository.update(this.game)

        val answersView = findViewById<TextView>(R.id.game_play_correct_answers)
        this.gameManager = GamePlayManagerFactory.dispatch(game)
        this.answers = this.gameManager.generateAnswers(game.allAnswersCount)
        answersView.text = this.gameManager.presentAnswers(this.answers)

        this.timeStarted = Clock.system(ZoneId.systemDefault()).millis()

        val timerView = findViewById<TextView>(R.id.game_play_timer)
        this.initTimer(game, timerView)
        this.timer.start()
    }

    override fun onResume(): Unit
    {
        super.onResume()

        if (this.timeHasPassed()) {
            this.processToAnswer()
        }
    }

    fun submit(view: View): Unit
    {
        this.processToAnswer()
    }

    private fun processToAnswer(): Unit
    {
        this.timer.cancel()

        val intent = Intent(this, GameAnswerActivity::class.java)
        intent.putExtra(ParamDictionary.CORRECT_ANSWERS_KEY, this.answers)
        intent.putExtra(ParamDictionary.GAME_ID_KEY, this.game._id)
        intent.putExtra(ParamDictionary.REAL_GAME_DURATION_KEY, this.getGameRealDuration())
        startActivity(intent)
    }

    private fun initTimer(game: Game, timerView: TextView): Unit
    {
        val timer = object: CountDownTimer(game.durationInSeconds.toLong() * 1000, 1000L)
        {
            lateinit var activity: GamePlayActivity

            override fun onTick(millisUntilFinished: Long): Unit
            {
                if (millisUntilFinished < 30000) {
                    timerView.setTextColor(Color.RED)
                }
                timerView.text = TimeUtil.longToTimeString(millisUntilFinished)
            }

            override fun onFinish(): Unit
            {
                this.activity.processToAnswer()
            }
        }

        timer.activity = this

        this.timer = timer
    }

    private fun timeHasPassed(): Boolean
    {
        return (Clock.system(ZoneId.systemDefault()).millis() - timeStarted) >= (this.game.durationInSeconds * 1000)
    }

    private fun getGameRealDuration(): Int
    {
        return minOf(((Clock.system(ZoneId.systemDefault()).millis() - timeStarted) / 1000).toInt(), this.game.durationInSeconds)
    }
}