package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.repository.GameRepository


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create database if wasn't created
        GameRepository(applicationContext, null).bootstrap()
    }

    fun debug(view: View): Unit
    {
        val intent = Intent(this, DebugActivity::class.java)

        startActivity(intent)
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

    fun about(view: View): Unit
    {
        val intent = Intent(this, AboutActivity::class.java)

        startActivity(intent)
    }
}