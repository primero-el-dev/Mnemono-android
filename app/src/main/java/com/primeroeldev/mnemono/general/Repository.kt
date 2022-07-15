package com.primeroeldev.mnemono.general

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.primeroeldev.mnemono.general.annotation.DatabaseColumn
import com.primeroeldev.mnemono.general.annotation.DatabaseTable
import com.primeroeldev.mnemono.toCamelCase
import java.util.ArrayList


abstract class Repository public constructor (
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
    private val entityClass: Class,
) : SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    companion object
    {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "mnemono.db"
    }

    override fun onCreate(db: SQLiteDatabase?): Unit
    {
        val table = this.getTableName()
        val sqlParts: ArrayList<String> = ArrayList<String>()

        for (field in this.entityClass.java.declaredFields) {
            val columnData = field.annotations.find { it -> it is DatabaseColumn } as? DatabaseColumn
            val name = if (columnData?.columnName !== "" && columnData?.columnName !== null)
                columnData.columnName
                else field.name.toCamelCase()
            val dataType = columnData?.dataType
            val length = columnData?.length.toString()
            val default = if (columnData?.defaultValue === null
                || (columnData.defaultValue as Double) == 0.0
                || columnData?.defaultValue == "")
                    ""
                    else "DEFAULT ${columnData.defaultValue}"
            val nullPart = if (columnData?.canBeNull == true) "NULL" else "NOT NULL"

            sqlParts.add(
                if (columnData?.id == true)
                    "${name} ${dataType}(${length}) ${nullPart} ${default}"
                    else "${name} ${dataType}(${length}) PRIMARY KEY"
            )
        }

        val query = "CREATE TABLE ${table} (${sqlParts.toArray().joinToString()})"

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int): Unit
    {
        TODO("Not yet implemented")
    }

    fun find(id: Int): EntityInterface?
    {
        val query = "SELECT * FROM ${this.getTableName()}"


    }

    private fun getTableName(): String?
    {
        return (this.entityClass.annotations.find { it -> it is DatabaseTable } as? DatabaseTable)?.tableName
    }

//    fun findBy(data: Any[]): EntityInterface[]
//
//    fun findOneBy(data: Any[]): EntityInterface[]
}