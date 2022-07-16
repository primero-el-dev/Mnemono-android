package com.primeroeldev.mnemono

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.game.GameType
import com.primeroeldev.mnemono.general.annotation.DatabaseColumn
import com.primeroeldev.mnemono.general.annotation.DatabaseTable
import com.primeroeldev.mnemono.validation.getErrorsOfGame
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val game = Game()
        game.id = 1
        game.type = GameType.WORDS
        game.createdAt = LocalDateTime.now()
        game.duration = LocalDateTime.now()
    }

    fun
}