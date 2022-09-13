package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.entity.Game
import com.primeroeldev.mnemono.repository.GameRepository
import com.primeroeldev.mnemono.game.manager.GamePlayManager
import com.primeroeldev.mnemono.game.manager.GamePlayManagerFactory


class GameAnswerActivity : AppActivity()
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
        this.gameManager = GamePlayManagerFactory.dispatch(this.game, applicationContext)

        findViewById<TextView>(R.id.game_play_answer_hint).text = this.gameManager.getAnswerInputHint()

        if (this.game.type != Game.CARDS_TYPE) {
            findViewById<TextView>(R.id.game_play_answer_poker_notation_help).visibility = View.INVISIBLE
        }
    }

    fun submit(view: View): Unit
    {
        val (checkedAnswers, correctCount) = this.gameManager
            .checkAnswers(this.correctAnswers, findViewById<TextView>(R.id.game_play_provide_answers).text.toString())

        this.game.correctAnswersCount = correctCount
        this.game.status = Game.FINISHED_STATUS
        this.gameRepository.update(this.game)

        val intent = Intent(this, GameResultActivity::class.java)
        intent.putExtra(ParamDictionary.CHECKED_ANSWERS_KEY, checkedAnswers)
        intent.putExtra(ParamDictionary.GAME_ID_KEY, this.game._id)
        startActivity(intent)
    }

    fun showPokerNotationHelp(view: View?): Unit
    {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.game_play_popup, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }
}