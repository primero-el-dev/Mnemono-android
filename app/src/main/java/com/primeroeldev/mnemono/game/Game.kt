package com.primeroeldev.mnemono.game

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.primeroeldev.mnemono.general.EntityInterface
import com.primeroeldev.mnemono.general.annotation.DatabaseColumn
import com.primeroeldev.mnemono.general.annotation.DatabaseId
import java.time.LocalDateTime


enum class GameType(val value: String)
{
    NUMBERS("numbers"),
    WORDS("words"),
    CARDS("cards"),
}

enum class GameStatus(val value: String)
{
    NOT_STARTED("not_started"),
    JUST_STARTED("just_started"),
    FINISHED("finished"),
}

@DatabaseTable(tableName = Game.TABLE_NAME)
class Game : EntityInterface
{
    companion object {
        const val TABLE_NAME = "game"
    }

    @DatabaseId
    override var id: Int? = null

    @DatabaseColumn(canBeNull = false)
    var status: GameStatus = GameStatus.NOT_STARTED

    @DatabaseColumn(dataType = "TINYINT", canBeNull = false)
    var includedInStatistics: Boolean = false

    @DatabaseColumn(dataType = "INT", canBeNull = false)
    var correctAnswersCount: Int = 0

    @DatabaseColumn(dataType = "INT", canBeNull = false)
    var allAnswersCount: Int = 0

    @DatabaseColumn(canBeNull = false)
    lateinit var type: GameType

    @DatabaseColumn(dataType = "TIME_STAMP", canBeNull = false)
    lateinit var createdAt: LocalDateTime

    @DatabaseColumn(dataType = "TIME_STAMP")
    lateinit var duration: LocalDateTime
}