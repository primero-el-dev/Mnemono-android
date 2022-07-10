package com.primeroeldev.mnemono.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.primeroeldev.mnemono.entity.EntityInterface


public const val DATABASE_VERSION = 1
public const val DATABASE_NAME = "mnemono.db"

abstract class Repository public constructor (
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
) : SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "mnemono.db"
    }

//    public abstract fun find(id: Int): EntityInterface?

//    public abstract fun findBy(data: Any[]): EntityInterface[]
//
//    public abstract fun findOneBy(data: Any[]): EntityInterface[]
}