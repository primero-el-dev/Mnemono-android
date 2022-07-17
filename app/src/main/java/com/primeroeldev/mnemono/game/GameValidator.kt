package com.primeroeldev.mnemono.validation

import com.primeroeldev.mnemono.game.Game


val validators: Map<String, Array<Pair<String, ((Any) -> Boolean)>>> = mapOf(
    "status" to arrayOf(
        Pair("game.validation.status.notEmpty", ::notEmpty),
        Pair("game.validation.status.inArray", { v: Any -> v is String && Game.getStatuses().contains(v) }),
    ),
    "correctAnswersCount" to arrayOf(
        Pair("game.validation.correctAnswersCount.min", { v: Any -> min(0.0)(v as Double) }),
        Pair("game.validation.correctAnswersCount.max", { v: Any -> max(99999999999.0)(v as Double) }),
    ),
    "allAnswersCount" to arrayOf(
        Pair("game.validation.allAnswersCount.min", { v: Any -> min(0.0)(v as Double) }),
        Pair("game.validation.allAnswersCount.max", { v: Any -> max(99999999999.0)(v as Double) }),
    ),
    "type" to arrayOf(
        Pair("game.validation.type.notEmpty", ::notEmpty),
        Pair("game.validation.type.inArray", { v: Any -> v is String && Game.getTypes().contains(v) }),
    ),
)

val getErrorsOfGame = getErrorsOf(validators)