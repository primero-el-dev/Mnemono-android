package com.primeroeldev.mnemono.entity

import java.time.LocalDateTime


enum class GameType
{
    NUMBERS_TYPE,
    WORDS_TYPE,
    CARDS_TYPE,
}

enum class GameStatus
{
    NOT_STARTED_STATUS,
    JUST_STARTED_STATUS,
    FINISHED_STATUS,
}

class Game
{
    public var id: Int? = null
    public lateinit var type: GameType
    public lateinit var createdAt: LocalDateTime
    public lateinit var duration: LocalDateTime
    public var status: GameStatus = GameStatus.NOT_STARTED_STATUS
    public var includedInStatistics: Boolean = false
    public var correctAnswersCount: Int = 0
    public var allAnswersCount: Int = 0
}