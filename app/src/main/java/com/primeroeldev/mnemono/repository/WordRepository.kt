package com.primeroeldev.mnemono.repository

import android.content.Context
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.entity.EntityInterface
import com.primeroeldev.mnemono.entity.Game
import com.primeroeldev.mnemono.entity.Word
import java.io.BufferedReader
import java.io.InputStreamReader


class WordRepository(
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
) : Repository(context, factory, Game::class.java.toString())
{
    override fun loadFixtures(): Unit
    {
        val file = BufferedReader(InputStreamReader(this.context?.resources?.openRawResource(R.raw.words)))
        val db = this.writableDatabase
        var portion: ArrayList<String> = ArrayList()
        var newToInsert = 0

        while (true) {
            val line = file.readLine() ?: break

            portion.add(line)
            newToInsert++
            if (newToInsert == 30) {
                val query = "INSERT INTO ${this.getTableName()} ('name') VALUES ('${portion.joinToString("'),('")}')"
                db.execSQL(query)
                portion = ArrayList()
            }
        }

        if (newToInsert > 0) {
            val query = "INSERT INTO ${this.getTableName()} (name) VALUES ('${portion.joinToString("'),('")}')"
            db.execSQL(query)
        }
    }

    fun findRandom(count: Int): ArrayList<EntityInterface>
    {
        // TODO: Check if works properly
        val db = this.readableDatabase
        val query = "SELECT * FROM ${this.getTableName()} ORDER BY RANDOM() LIMIT ${count}"
        val cursor = db.rawQuery(query, null)

        return this.getEntitiesFromCursor(cursor)
    }

    protected override fun getClassInstance(): EntityInterface
    {
        return Word()
    }
}