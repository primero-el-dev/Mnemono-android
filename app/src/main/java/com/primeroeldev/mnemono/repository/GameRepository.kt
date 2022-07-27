package com.primeroeldev.mnemono.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.primeroeldev.mnemono.entity.Game
import com.primeroeldev.mnemono.entity.EntityInterface

class GameRepository(
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
) : Repository(context, factory, Game::class.java.toString())
{
    protected override fun getClassInstance(): EntityInterface
    {
        return Game()
    }
}