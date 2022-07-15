package com.primeroeldev.mnemono.general

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.primeroeldev.mnemono.general.annotation.DatabaseColumn
import com.primeroeldev.mnemono.general.annotation.DatabaseId
import com.primeroeldev.mnemono.general.annotation.DatabaseTable
import kotlin.collections.ArrayList


abstract class Repository public constructor (
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
    private val entityClass: Class,
) : SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    companion object
    {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "mnemono.db"
    }

    override fun onCreate(db: SQLiteDatabase?): Unit
    {
        val table = this.getTableName()
        val sqlParts: ArrayList<String> = ArrayList<String>()

        for (field in this.entityClass.java.declaredFields) {
            val columnData = field.annotations.find { it -> it is DatabaseColumn } as? DatabaseColumn
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
                    "${field.name} ${dataType}(${length}) ${nullPart} ${default}"
                    else "${field.name} ${dataType}(${length}) PRIMARY KEY"
            )
        }

        val query = "CREATE TABLE ${table} (${sqlParts.toArray().joinToString()})"

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int): Unit
    {
        db?.execSQL("DROP TABLE IF EXISTS ${this.getTableName()}")

        this.onCreate(db)
    }

    fun find(id: Int): EntityInterface?
    {
        val query = "SELECT * FROM ${this.getTableName()}"
    }

    fun insert(entity: EntityInterface): Long
    {
        val db = this.writableDatabase
        val contentValues: ContentValues = this.getContentValues(entity)

        val success = db.insert(this.getTableName(), null, contentValues)
        db.close()

        return success
    }

    fun update(entity: EntityInterface): Int
    {
        val db = this.writableDatabase
        val idColumn = this.entityClass::class.members.filter { it -> it.findAnnotation<DatabaseId>() != null }
        val contentValues: ContentValues = this.getContentValues(entity)
        val whereArgs: Array<String> = arrayOf(readInstanceProperty(entity, idColumn))

        val success = db.update(this.getTableName(), contentValues, "${idColumn} = ?", whereArgs)
        db.close()

        return success
    }

    fun delete(entity: EntityInterface): Int
    {
        val db = this.writableDatabase
        val idColumn = this.entityClass::class.members.filter { it -> it.findAnnotation<DatabaseId>() != null }
        val whereArgs: Array<String> = arrayOf(readInstanceProperty(entity, idColumn))

        val success = db.delete(this.getTableName(), "${idColumn} = ?", whereArgs)
        db.close()

        return success
    }

    private fun getTableName(): String?
    {
        return (this.entityClass.annotations.find { it -> it is DatabaseTable } as? DatabaseTable)?.tableName
    }

    private fun getContentValues(entity: EntityInterface): ContentValues
    {
        val contentValues = ContentValues()
        val columns = this.entityClass::class.members.filter { it -> it.findAnnotation<DatabaseColumn>() != null }

        for (column in columns) {
            contentValues.put(column, readInstanceProperty(entity, column))
        }

        return contentValues
    }

//    fun findBy(data: Any[]): EntityInterface[]
//
//    fun findOneBy(data: Any[]): EntityInterface[]
}