package com.primeroeldev.mnemono.validation

import com.primeroeldev.mnemono.game.Game


val validators: Map<String, Array<Pair<String, ((Any) -> Boolean)>>> = mapOf(
    "status" to arrayOf(
        Pair("game_validation_status_notEmpty", ::notEmpty),
        Pair("game_validation_status_inArray", { v: Any -> v is String && Game.getStatuses().contains(v) }),
    ),
    "correctAnswersCount" to arrayOf(
        Pair("game_validation_correctAnswersCount_min", { v: Any -> min(0.0)(v as Double) }),
        Pair("game_validation_correctAnswersCount_max", { v: Any -> max(99999999999.0)(v as Double) }),
    ),
    "allAnswersCount" to arrayOf(
        Pair("game_validation_allAnswersCount_min", { v: Any -> min(0.0)(v as Double) }),
        Pair("game_validation_allAnswersCount_max", { v: Any -> max(99999999999.0)(v as Double) }),
    ),
    "type" to arrayOf(
        Pair("game_validation_type_notEmpty", ::notEmpty),
        Pair("game_validation_type_inArray", { v: Any -> v is String && Game.getTypes().contains(v) }),
    ),
    "durationInSeconds" to arrayOf(
        Pair("game_validation_durationInSeconds_min", ::notEmpty),
        Pair("game_validation_durationInSeconds_max", { v: Any -> v is Int && v < 24*3600 }),
    ),
)

val getErrorsOfGame = getErrorsOf(validators)