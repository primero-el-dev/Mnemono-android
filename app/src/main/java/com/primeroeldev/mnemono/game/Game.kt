package com.primeroeldev.mnemono.game

import com.primeroeldev.mnemono.general.EntityInterface
import com.primeroeldev.mnemono.general.annotation.DatabaseColumn
import com.primeroeldev.mnemono.general.annotation.DatabaseId
import com.primeroeldev.mnemono.general.annotation.DatabaseTable
import java.time.LocalDateTime


@DatabaseTable(tableName = Game.TABLE_NAME)
class Game : EntityInterface
{
    companion object {
        const val TABLE_NAME = "game"

        const val NUMBERS_TYPE = "numbers"
        const val WORDS_TYPE = "words"
        const val CARDS_TYPE = "cards"

        const val NOT_STARTED_STATUS = "not_started"
        const val JUST_STARTED_STATUS = "just_started"
        const val FINISHED_STATUS = "finished"

        fun getTypes(): Array<String> = arrayOf(
            NUMBERS_TYPE,
            WORDS_TYPE,
            CARDS_TYPE,
        )

        fun getStatuses(): Array<String> = arrayOf(
            NOT_STARTED_STATUS,
            JUST_STARTED_STATUS,
            FINISHED_STATUS,
        )
    }

    @DatabaseId
    override var id: Int? = null

    @DatabaseColumn(canBeNull = false)
    var status: String = NOT_STARTED_STATUS

    @DatabaseColumn(dataType = "TINYINT", canBeNull = false)
    var includedInStatistics: Boolean = false

    @DatabaseColumn(dataType = "INT", canBeNull = false)
    var correctAnswersCount: Int = 0

    @DatabaseColumn(dataType = "INT", canBeNull = false)
    var allAnswersCount: Int = 0

    @DatabaseColumn(canBeNull = false)
    lateinit var type: String

    @DatabaseColumn(dataType = "TIME_STAMP", canBeNull = false)
    lateinit var createdAt: LocalDateTime

    @DatabaseColumn(dataType = "TIME_STAMP")
    lateinit var duration: LocalDateTime
}