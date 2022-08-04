package com.primeroeldev.mnemono.entity

import com.primeroeldev.mnemono.annotation.DatabaseColumn
import com.primeroeldev.mnemono.annotation.DatabaseId
import com.primeroeldev.mnemono.annotation.DatabaseTable


@DatabaseTable(tableName = Word.TABLE_NAME)
class Word : EntityInterface
{
    companion object
    {
        const val TABLE_NAME = "word"
    }

    @DatabaseId
    override var _id: Long? = null

    @DatabaseColumn(canBeNull = false, length = 255)
    lateinit var name: String
}