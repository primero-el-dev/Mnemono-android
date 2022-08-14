package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.repository.GameRepository
import com.primeroeldev.mnemono.repository.WordRepository
import java.util.*


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GameRepository(applicationContext, null).bootstrap()
        WordRepository(applicationContext, null).bootstrap()
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