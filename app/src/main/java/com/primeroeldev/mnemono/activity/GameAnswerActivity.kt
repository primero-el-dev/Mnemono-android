package com.primeroeldev.mnemono.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.primeroeldev.mnemono.R

class GameAnswerActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_answer)
    }

    fun answer(view: View): Unit
    {
//        val intent = Intent(this, GameAnswerActivity::class.java)
//        intent.putExtra(ParamDictionary.CHECKED_ANSWERS_KEY, this.answers)
//        intent.putExtra(ParamDictionary.CORRECT_ANSWERS_COUNT_KEY, 0)
//        startActivity(intent)
    }
}