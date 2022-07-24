package com.primeroeldev.mnemono.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.game.GameRepository

class GameIndexActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_index)

        val games = GameRepository(applicationContext, null).findAll()

        
    }
}