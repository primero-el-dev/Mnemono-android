package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.game.GameRepository


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startGame(view: View)
    {
        val intent = Intent(this, GameStartActivity::class.java)

        startActivity(intent)
    }

    fun goToDebug(view: View)
    {
        val intent = Intent(this, DebugActivity::class.java)

        startActivity(intent)
    }
}