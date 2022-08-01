package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startGame(view: View): Unit
    {
        val intent = Intent(this, GameStartActivity::class.java)

        startActivity(intent)
    }

    fun statistics(view: View): Unit
    {
        val intent = Intent(this, GameStatisticsActivity::class.java)

        startActivity(intent)
    }

    fun listGames(view: View): Unit
    {
        val intent = Intent(this, GameIndexActivity::class.java)

        startActivity(intent)
    }

    fun goToDebug(view: View): Unit
    {
        val intent = Intent(this, DebugActivity::class.java)

        startActivity(intent)
    }
}