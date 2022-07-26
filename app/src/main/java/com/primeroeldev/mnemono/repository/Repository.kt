package com.primeroeldev.mnemono.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.primeroeldev.mnemono.annotation.DatabaseColumn
import com.primeroeldev.mnemono.annotation.DatabaseId
import com.primeroeldev.mnemono.annotation.DatabaseTable
import com.primeroeldev.mnemono.entity.EntityInterface
import com.primeroeldev.mnemono.general.readInstanceProperty
import com.primeroeldev.mnemono.general.writeInstanceProperty
import com.primeroeldev.mnemono.validation.notEmpty
import kotlin.collections.ArrayList


abstract class Repository (
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
    protected val entityClass: String
) : SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    companion object
    {
        protected const val DATABASE_VERSION = 1
        protected const val DATABASE_NAME = "mnemono.db"
    }

    override fun onCreate(db: SQLiteDatabase?): Unit
    {
        val sqlParts: ArrayList<String> = ArrayList()

        for (field in this.getClassInstance()::class.java.declaredFields) {
            val columnData = field.annotations.filter { it is DatabaseColumn }.firstOrNull() as? DatabaseColumn
                ?: continue
            val dataType = columnData.dataType
            val length = columnData.length.toString()
            val default = "DEFAULT ${
                if (columnData.defaultValue::class.simpleName == "String") "'${columnData.defaultValue}'"
                else columnData.defaultValue
            }"
            val nullPart = if (columnData.canBeNull == true) "NULL" else "NOT NULL"

            sqlParts.add("${field.name} ${dataType}(${length}) ${nullPart} ${default}")
        }

        sqlParts.add("${this.getIdProperty()!!} INTEGER PRIMARY KEY AUTOINCREMENT")

        val query = "CREATE TABLE ${this.getTableName()} (${sqlParts.toArray().joinToString(", ")})"

        db?.execSQL(query)

        this.loadFixtures()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int): Unit
    {
        db?.execSQL("DROP TABLE IF EXISTS ${this.getTableName()}")

        this.onCreate(db)
    }

    fun findAll(): ArrayList<EntityInterface>
    {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${this.getTableName()}"
        val cursor = db.rawQuery(query, null)

        return this.getEntitiesFromCursor(cursor)
    }

    fun findBy(criteria: ArrayList<Pair<String, Any?>>, order: String = ""): ArrayList<EntityInterface>
    {
        val db = this.readableDatabase
        val selections: ArrayList<String> = ArrayList()
        val selectionAgrs: ArrayList<String> = ArrayList()
        val neededColumns = this.getMappedFields()
        neededColumns.add("rowid")

        for ((wherePart, value) in criteria) {
            val arg = if (value is String) value
                else value.toString()
            selectionAgrs.add(arg)

            if (wherePart.matches("""^\w+$""".toRegex())) {
                selections.add("($wherePart = ?)")
            }
            else {
                selections.add("($wherePart)")
            }
        }

        val cursor = db.query(
            this.getTableName(),
            neededColumns.toTypedArray(),
            selections.joinToString(" AND "),
            selectionAgrs.toTypedArray(),
            "",
            "",
            order
        )

        return this.getEntitiesFromCursor(cursor)
    }

    fun findOneBy(criteria: ArrayList<Pair<String, Any?>>, order: String = ""): EntityInterface?
    {
        return this.findBy(criteria).firstOrNull()
    }

    fun find(id: Long): Any?
    {
        val criteria: ArrayList<Pair<String, Any?>> = ArrayList()
        if (this.getIdProperty() != null) {
            criteria.add(Pair(this.getIdProperty()!!, id))
        }

        return this.findOneBy(criteria)
    }

    fun count(): Int
    {
        return this.findAll().size
    }

    fun countBy(criteria: ArrayList<Pair<String, Any?>>): Int
    {
        return this.findBy(criteria).size
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
        val idProperty = this.getIdProperty()
        val whereArgs: Array<String> = arrayOf(String.format("%d", readInstanceProperty(entity, idProperty!!)))

        return this.updateBy(entity, "${idProperty} = ?", whereArgs)
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
        val idProperty = this.getIdProperty()
        val whereArgs: Array<String> = arrayOf(String.format("%d", readInstanceProperty(entity, idProperty!!)))

        return this.deleteBy("${idProperty} = ?", whereArgs)
    }

    protected open fun loadFixtures(): Unit
    {

    }

    protected open fun getClassInstance(): EntityInterface
    {
        return Class.forName(this.entityClass).kotlin.objectInstance as EntityInterface
    }

    protected fun getTableName(): String?
    {
        return (this.getClassInstance()::class.annotations.find { it is DatabaseTable } as? DatabaseTable)?.tableName
    }

    protected fun getIdProperty(): String?
    {
        return this.getClassInstance()::class.java.declaredFields
            .filter { it.getAnnotation(DatabaseId::class.java) != null }
            .map { it.name }
            .first()
    }

    protected fun getMappedFields(): ArrayList<String>
    {
        return this.getClassInstance()::class.java.declaredFields
            .filter { it.getAnnotation(DatabaseColumn::class.java) != null }
            .map { it.name }
                as ArrayList
    }

    protected fun getContentValues(entity: EntityInterface): ContentValues
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
                    "Integer" -> contentValues.put(column, value as Int)
                    "Double" -> contentValues.put(column, value as Double)
                    "Float" -> contentValues.put(column, value as Float)
                    "Long" -> contentValues.put(column, value as Long)
                    "Boolean" -> contentValues.put(column, if (notEmpty(value)) 1 else 0)
                    else -> contentValues.put(column, value as String)
                }
            }
        }

        return contentValues
    }

    protected fun getEntitiesFromCursor(cursor: Cursor): ArrayList<EntityInterface>
    {
        val entities: ArrayList<EntityInterface> = ArrayList()

        if (!cursor.moveToFirst()) {
            return ArrayList()
        }

        do {
            entities.add(this.rowToEntity(cursor))
        } while (cursor.moveToNext())

        return entities
    }

    @SuppressLint("Range")
    protected fun rowToEntity(cursor: Cursor): EntityInterface
    {
        val idProperty = this.getIdProperty()
        val entity: EntityInterface = this.getClassInstance()
        val idIndex = cursor.getColumnIndex(idProperty)

        if (idProperty != null) {
            writeInstanceProperty(entity, idProperty, cursor.getInt(idIndex).toLong())
        }

        for (field in this.getClassInstance()::class.java.declaredFields) {
            val columnData = field.annotations.find { it is DatabaseColumn } as? DatabaseColumn
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