package com.primeroeldev.mnemono.entity

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import kotlin.properties.Delegates


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

@DatabaseTable(tableName = "game")
class Game : EntityInterface
{
    @DatabaseField(id = true, generatedId = true)
    public override var id: Int? = null

    @DatabaseField(canBeNull = false)
    public var status: GameStatus = GameStatus.NOT_STARTED

    @DatabaseField(dataType = DataType.BOOLEAN, canBeNull = false)
    public var includedInStatistics: Boolean = false

    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
    public var correctAnswersCount: Int = 0

    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
    public var allAnswersCount: Int = 0

    @DatabaseField(canBeNull = false)
    public lateinit var type: GameType

    @DatabaseField(dataType = DataType.TIME_STAMP, canBeNull = false)
    public lateinit var createdAt: LocalDateTime

    @DatabaseField(dataType = DataType.TIME_STAMP)
    public lateinit var duration: LocalDateTime
}