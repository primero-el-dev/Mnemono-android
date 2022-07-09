package com.primeroeldev.mnemono.entity

import java.time.LocalDateTime


enum class GameType
{
    NUMBERS,
    WORDS,
    CARDS,
}

enum class GameStatus
{
    NOT_STARTED,
    JUST_STARTED,
    FINISHED,
}

class Game
{
    public var id: Int? = null
    public var status: GameStatus = GameStatus.NOT_STARTED
    public var includedInStatistics: Boolean = false
    public var correctAnswersCount: Int = 0
    public var allAnswersCount: Int = 0
    public lateinit var type: GameType
    public lateinit var createdAt: LocalDateTime
    public lateinit var duration: LocalDateTime
}