package com.primeroeldev.mnemono.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase

class GameRepository public constructor(
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
) : Repository(context, factory)
{
    override fun onCreate(db: SQLiteDatabase?)
    {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        TODO("Not yet implemented")
    }

}