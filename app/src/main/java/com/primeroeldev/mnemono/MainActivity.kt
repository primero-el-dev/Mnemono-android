package com.primeroeldev.mnemono

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.primeroeldev.mnemono.entity.Game

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var game = Game()
        game.id = 1

    }
}