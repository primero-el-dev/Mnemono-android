package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val itemsCount = 10
//        val url = URL("https://random-word-api.herokuapp.com/word?lang=en&number=$itemsCount")
//        val connection = url.openConnection() as HttpURLConnection
//        connection.setRequestMethod("GET")
//        connection.connect()

        resources.openRawResource(R.raw.words)
    }

    fun startGame(view: View): Unit
    {
        val intent = Intent(this, GameStartActivity::class.java)

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