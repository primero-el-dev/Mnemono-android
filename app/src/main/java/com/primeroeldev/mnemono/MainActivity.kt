package com.primeroeldev.mnemono

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.activity.GameStartActivity


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
}