package com.primeroeldev.mnemono.general

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.primeroeldev.mnemono.general.annotation.DatabaseColumn
import com.primeroeldev.mnemono.general.annotation.DatabaseId
import com.primeroeldev.mnemono.general.annotation.DatabaseTable
import java.lang.reflect.Field
import kotlin.collections.ArrayList
import kotlin.reflect.KClass


abstract class Repository public constructor (
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
    private val entityClass: Class<Any>,
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

        for (field in this.entityClass::class.java.declaredFields) {
            val columnData = field.annotations.find { it -> it is DatabaseColumn } as? DatabaseColumn
                ?: continue
            val dataType = columnData.dataType
            val length = columnData.length.toString()
            val default = if ((columnData.defaultValue as Double) == 0.0
                || columnData.defaultValue == "")
                    ""
                    else "DEFAULT ${columnData.defaultValue}"
            val nullPart = if (columnData.canBeNull == true) "NULL" else "NOT NULL"

            sqlParts.add(
                if (columnData.id == true)
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

    fun findAll(): ArrayList<Class<Any>>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${this.getTableName()}"

        try {
            val cursor = db.rawQuery(query, null)

            return this.getEntitiesFromCursor(cursor)
        }
        catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(query)

            return ArrayList()
        }
    }

    fun findBy(criteria: ArrayList<Pair<String, Any?>>): ArrayList<Class<Any>>
    {
        val db = this.readableDatabase
        var selection = ""
        var selectionAgrs: ArrayList<String> = ArrayList()

        for ((wherePart, value) in criteria) {
            val arg = if (value is String) value
                else value.toString()
            selectionAgrs.add(arg)

            if (wherePart.matches("""^\w+$""".toRegex())) {
                selection += " " + wherePart + " = ?"
            }
            else {
                selection += " " + wherePart
            }
        }

        try {
            val cursor = db.query(
                this.getTableName(),
                this.getMappedFields().toTypedArray(),
                selection,
                selectionAgrs.toTypedArray(),
                "",
                "",
                ""
            )

            return this.getEntitiesFromCursor(cursor)
        }
        catch (e: Exception) {
            e.printStackTrace()

            return ArrayList()
        }
    }

    fun findOneBy(criteria: ArrayList<Pair<String, Any?>>): Class<Any>?
    {
        val entities = this.findBy(criteria)

        if (entities.isEmpty()) {
            return null
        }
        else {
            return entities.first()
        }
    }

    fun find(id: Int): Class<Any>?
    {
        val criteria: ArrayList<Pair<String, Any?>> = ArrayList()
        if (this.getIdColumn() != null) {
            criteria.add(Pair(this.getIdColumn() as String, id))
        }

        return this.findOneBy(criteria)
    }

    fun insert(entity: EntityInterface): Long
    {
        val db = this.writableDatabase
        val contentValues: ContentValues = this.getContentValues(entity)

        val id = db.insert(this.getTableName(), null, contentValues)
        db.close()

        return id
    }

    fun updateBy(entity: EntityInterface, where: String, whereArgs: Array<String>): Int
    {
        val db = this.writableDatabase
        val contentValues: ContentValues = this.getContentValues(entity)

        val success = db.update(this.getTableName(), contentValues, where, whereArgs)
        db.close()

        return success
    }

    fun update(entity: EntityInterface): Int
    {
        val idColumn = this.getIdColumn()
        if (idColumn == null) {
            return 0
        }
        val whereArgs: Array<String> = arrayOf(readInstanceProperty(entity, idColumn))

        return this.updateBy(entity, "${idColumn} = ?", whereArgs)
    }

    fun deleteBy(where: String, whereArgs: Array<String>): Int
    {
        val db = this.writableDatabase

        val success = db.delete(this.getTableName(), where, whereArgs)
        db.close()

        return success
    }

    fun delete(entity: EntityInterface): Int
    {
        val idColumn = this.getIdColumn()
        val whereArgs: Array<String> = arrayOf(readInstanceProperty(entity, idColumn as String))

        return this.deleteBy("${idColumn} = ?", whereArgs)
    }

    private fun getTableName(): String?
    {
        return (this.entityClass.annotations.find { it -> it is DatabaseTable } as? DatabaseTable)?.tableName
    }

    private fun getIdColumn(): String?
    {
        return this.entityClass::class.java.fields
            .filter { it.getAnnotation(DatabaseId::class.java) != null }
            .map { it.name }
            .first()
    }

    private fun getMappedFields(): ArrayList<String>
    {
        return this.entityClass::class.java.fields
            .filter { it.getAnnotation(DatabaseColumn::class.java) != null }
            .map { it.name }
                as ArrayList
    }

    private fun getContentValues(entity: EntityInterface): ContentValues
    {
        val contentValues = ContentValues()
        val columns = this.getMappedFields()

        for (column in columns) {
            val value = readInstanceProperty<Any>(entity, column)
            if (value == null) {
                contentValues.putNull(column)
            }
            else {
                when (value::class.simpleName) {
                    "String" -> contentValues.put(column, value as String)
                    "Int" -> contentValues.put(column, value as Int)
                    "Double" -> contentValues.put(column, value as Double)
                    "Long" -> contentValues.put(column, value as Long)
                    else -> contentValues.put(column, value as String)
                }
            }
        }

        return contentValues
    }

    private fun getEntitiesFromCursor(cursor: Cursor): ArrayList<Class<Any>>
    {
        val entities: ArrayList<Class<Any>> = ArrayList()

        if (!cursor.moveToFirst()) {
            return ArrayList()
        }

        do {
            entities.add(this.rowToEntity(cursor))
        } while (cursor.moveToNext())

        return entities
    }

    @SuppressLint("Range")
    private fun rowToEntity(cursor: Cursor): Class<Any>
    {
        val idColumn = this.getIdColumn()
        val fields = this.getMappedFields()
        val entity = this.entityClass

        if (idColumn != null) {
            fields.add(idColumn)
        }

        for (field in this.entityClass.declaredFields) {
            val columnData = field.annotations.find { it -> it is DatabaseColumn } as? DatabaseColumn
                ?: continue
            val index = cursor.getColumnIndex(field.name)
            val value = when (columnData.dataType) {
                "INTEGER" -> cursor.getInt(index)
                "REAL" -> cursor.getDouble(index)
                else -> cursor.getString(index)
            }
            
            writeInstanceProperty(entity, field.name, value)
        }

        return entity
    }
}