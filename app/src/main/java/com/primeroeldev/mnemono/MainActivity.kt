package com.primeroeldev.mnemono

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.game.GameType
import com.primeroeldev.mnemono.general.annotation.DatabaseColumn
import com.primeroeldev.mnemono.general.annotation.DatabaseTable
import com.primeroeldev.mnemono.validation.getErrorsOfGame
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val game = Game()
        game.id = 1
        game.type = GameType.WORDS
        game.createdAt = LocalDateTime.now()
        game.duration = LocalDateTime.now()


        val table = (Game::class.annotations.find { it is DatabaseTable } as? DatabaseTable)?.tableName
        val sqlParts: ArrayList<String> = ArrayList<String>()

        for (field in Game::class.java.declaredFields) {
            val columnData = field.annotations.find { it is DatabaseColumn } as? DatabaseColumn
            val name = if (columnData?.columnName !== "" && columnData?.columnName !== null)
                columnData.columnName
                else field.name.toCamelCase()
            val dataType = columnData?.dataType
            val length = columnData?.length.toString()
            val default =
                if (columnData?.defaultValue === null
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

        getErrorsOfGame(game)

        println(query)
    }
}