package com.primeroeldev.mnemono.game

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.primeroeldev.mnemono.general.EntityInterface
import com.primeroeldev.mnemono.general.Repository

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