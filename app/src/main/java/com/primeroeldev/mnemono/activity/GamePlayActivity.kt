package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.entity.Game
import com.primeroeldev.mnemono.game.manager.GamePlayManager
import com.primeroeldev.mnemono.game.manager.GamePlayManagerFactory
import com.primeroeldev.mnemono.general.TimeUtil
import com.primeroeldev.mnemono.repository.GameRepository


class GamePlayActivity : AppActivity()
{
    private lateinit var gameManager: GamePlayManager
    private lateinit var answers: String
    private lateinit var game: Game
    private lateinit var timer: CountDownTimer
    private lateinit var gameRepository: GameRepository
    private var timeStarted: Long = 0
    private var gameId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?): Unit
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        this.gameRepository = GameRepository(applicationContext, null)
        val gameId = this.intent?.getLongExtra(ParamDictionary.GAME_ID_KEY, 0)!!
        val game = this.gameRepository.find(gameId) as? Game

        if (gameId == 0L || game == null) {
            val intent = Intent(this, GameStartActivity::class.java)
            startActivity(intent)
        }

        this.game = game!!

        this.game.status = Game.JUST_STARTED_STATUS
        this.gameRepository.update(this.game)

        this.gameManager = GamePlayManagerFactory.dispatch(game, applicationContext)
        this.answers = this.gameManager.generateAnswers(game.allAnswersCount)

        if (this.gameManager.getInputType() == GamePlayManager.TEXT_INPUT_TYPE) {
            findViewById<NestedScrollView>(R.id.game_play_text_scroll_correct_answers).visibility = View.VISIBLE
            val answersView = findViewById<TextView>(R.id.game_play_text_correct_answers)
            answersView.text = this.gameManager.presentAnswersText(this.answers)
        }
        else if (this.gameManager.getInputType() == GamePlayManager.IMAGE_INPUT_TYPE) {
            findViewById<HorizontalScrollView>(R.id.game_play_image_scroll_correct_answers).visibility = View.VISIBLE
            val answersView = findViewById<ImageView>(R.id.game_play_image_correct_answers)
            answersView.setImageBitmap(this.gameManager.presentAnswersImage(this.answers))
        }

        this.timeStarted = System.currentTimeMillis()

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

    override fun onBackPressed(): Unit
    {
        this.timer.cancel()

        this.game.durationInSeconds = this.getGameRealDuration()
        this.gameRepository.update(this.game)

        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        this.timer.cancel()

        this.game.durationInSeconds = this.getGameRealDuration()
        this.gameRepository.update(this.game)

        return super.onOptionsItemSelected(item)
    }

    fun submit(view: View): Unit
    {
        this.processToAnswer()
    }

    private fun processToAnswer(): Unit
    {
        this.timer.cancel()

        this.game.durationInSeconds = this.getGameRealDuration()
        this.gameRepository.update(this.game)

        this.finish()

        val intent = Intent(this, GameAnswerActivity::class.java)
        intent.putExtra(ParamDictionary.CORRECT_ANSWERS_KEY, this.answers)
        intent.putExtra(ParamDictionary.GAME_ID_KEY, this.game._id)
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
        return (System.currentTimeMillis() - timeStarted) >= (this.game.durationInSeconds * 1000)
    }

    private fun getGameRealDuration(): Int
    {
        return minOf(
            ((System.currentTimeMillis() - timeStarted) / 1000).toInt(),
            this.game.durationInSeconds
        )
    }
}