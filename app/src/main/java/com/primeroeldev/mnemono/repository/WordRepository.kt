package com.primeroeldev.mnemono.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.primeroeldev.mnemono.entity.EntityInterface
import com.primeroeldev.mnemono.entity.Game
import com.primeroeldev.mnemono.entity.Word
import com.primeroeldev.mnemono.game.words
import java.io.File


class WordRepository(
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
) : Repository(context, factory, Game::class.java.toString())
{
    companion object
    {
        const val FIXTURES_PATH = ""
    }

    fun findRandom(count: Int): ArrayList<EntityInterface>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${this.getTableName()} ORDER BY RANDOM() LIMIT ${count}"
        val cursor = db.rawQuery(query, null)

        return this.getEntitiesFromCursor(cursor)
    }

    override fun getClassInstance(): EntityInterface
    {
        return Word()
    }

    override fun loadFixtures(): Unit
    {




        val db = this.writableDatabase
        var portion: ArrayList<String> = ArrayList()
        var newToInsert = 0

        File(FIXTURES_PATH).forEachLine {
            portion.add(it)
            newToInsert++
            if (newToInsert == 100) {
                val query = "INSERT INTO ${this.getTableName()} (name) VALUES ('${portion.joinToString("','")}')"
                db.execSQL(query)
                portion = ArrayList()
            }
        }

        if (newToInsert > 0) {
            val query = "INSERT INTO ${this.getTableName()} (name) VALUES ('${portion.joinToString("','")}')"
            db.execSQL(query)
        }

//        for (wordsChunked in words.chunked(40)) {
//            if (wordsChunked.size <= 0) {
//                continue
//            }
//            val query = "INSERT INTO ${this.getTableName()} (name) VALUES ('${wordsChunked.joinToString("','")}')"
//            db.execSQL(query)
//        }
    }
}