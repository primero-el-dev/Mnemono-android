package com.primeroeldev.mnemono.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.game.GameRepository
import com.primeroeldev.mnemono.game.manager.GamePlayManager
import com.primeroeldev.mnemono.game.manager.GamePlayManagerFactory

class GameAnswerActivity : AppCompatActivity()
{
    private lateinit var correctAnswers: String
    private lateinit var gameRepository: GameRepository
    private lateinit var game: Game
    private lateinit var gameManager: GamePlayManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_answer)

        this.correctAnswers = this.intent.getStringExtra(ParamDictionary.CORRECT_ANSWERS_KEY)!!
        val gameId = this.intent.getLongExtra(ParamDictionary.GAME_ID_KEY, 0)
        this.gameRepository = GameRepository(applicationContext, null)

        val game = this.gameRepository.find(gameId) as? Game
        if (game == null) {
            val intent = Intent(this, GameStartActivity::class.java)
            startActivity(intent)
        }

        this.game = game!!
        this.game.durationInSeconds = this.intent.getIntExtra(ParamDictionary.REAL_GAME_DURATION_KEY, 0)
        this.gameRepository.update(this.game)
        this.gameManager = GamePlayManagerFactory.dispatch(this.game)

        (findViewById(R.id.game_play_answer_hint) as TextView).text = this.gameManager.getAnswerInputHint()
    }

    fun submit(view: View): Unit
    {
        val (checkedAnswers, correctCount) = this.gameManager
            .checkAnswers(this.correctAnswers, (findViewById(R.id.game_play_provide_answers) as TextView).text.toString())

        this.game.correctAnswersCount = correctCount
        this.game.status = Game.FINISHED_STATUS
        this.gameRepository.update(this.game)

        val intent = Intent(this, GameResultActivity::class.java)
        intent.putExtra(ParamDictionary.CHECKED_ANSWERS_KEY, checkedAnswers)
        intent.putExtra(ParamDictionary.GAME_ID_KEY, this.game._id)
        startActivity(intent)
    }
}