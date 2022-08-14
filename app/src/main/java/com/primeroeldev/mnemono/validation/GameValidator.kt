package com.primeroeldev.mnemono.validation

import com.primeroeldev.mnemono.entity.Game


fun getErrorsFor(game: Game): Map<String, String>
{
    val errors: MutableMap<String, String> = mutableMapOf()

    if (!Game.getStatuses().contains(game.status)) {
        errors.put("status", "game_validation_status_inArray")
    }

    if (!Game.getTypes().contains(game.type)) {
        errors.put("type", "game_validation_type_inArray")
    }

    val itemsCountLimit = when (game.type) {
        Game.NUMBERS_TYPE -> 99999
        Game.WORDS_TYPE -> 99999
        Game.CARDS_TYPE -> 340
        else -> 0
    }

    if (game.allAnswersCount < 1) {
        errors.put("allAnswersCount", "game_validation_allAnswersCount_min")
    }
    else if (game.allAnswersCount > itemsCountLimit) {
        errors.put("allAnswersCount", "game_validation_allAnswersCount_max")
    }

    if (game.durationInSeconds < 10) {
        errors.put("durationInSeconds", "game_validation_durationInSeconds_min")
    }
    else if (game.durationInSeconds > Game.MAX_DURATION) {
        errors.put("durationInSeconds", "game_validation_durationInSeconds_max")
    }

    return errors
}