package com.primeroeldev.mnemono.validation

import com.primeroeldev.mnemono.entity.GameStatus
import com.primeroeldev.mnemono.entity.GameType


val validators: Map<String, Array<Pair<String, ((Any) -> Boolean)>>> = mapOf(
    "status" to arrayOf(
        Pair("game.validation.status.notEmpty", { v: Any -> notEmpty(v) }),
        Pair("game.validation.status.inArray", { v: Any -> inArray(GameStatus.values())(v) }),
        Pair("game.validation.status.minLength", { v: Any -> minLength(3)(v) }),
        Pair("game.validation.status.maxLength", { v: Any -> maxLength(255)(v) }),
    ),
    "correctAnswersCount" to arrayOf(
        Pair("game.validation.correctAnswersCount.min", { v: Any -> min(0)(v) }),
        Pair("game.validation.correctAnswersCount.max", { v: Any -> max(99999999999)(v) }),
    ),
    "allAnswersCount" to arrayOf(
        Pair("game.validation.allAnswersCount.min", { v: Any -> min(0)(v) }),
        Pair("game.validation.allAnswersCount.max", { v: Any -> max(99999999999)(v) }),
    ),
    "type" to arrayOf(
        Pair("game.validation.type.notEmpty", { v: Any -> notEmpty(v) }),
        Pair("game.validation.type.inArray", { v: Any -> inArray(GameType.values())(v) }),
        Pair("game.validation.type.minLength", { v: Any -> minLength(3)(v) }),
        Pair("game.validation.type.maxLength", { v: Any -> maxLength(255)(v) }),
    ),
)

val getErrorsFor = getErrorsOf(validators)