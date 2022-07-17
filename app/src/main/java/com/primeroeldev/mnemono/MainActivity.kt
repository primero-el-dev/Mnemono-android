package com.primeroeldev.mnemono

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.general.readInstanceProperty


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        val game = Game()
//        game.status = "Status"
//        val value = readInstanceProperty<Any>(game, "status")
//
//        val v = 1
//        val s = (v.javaClass) value

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}