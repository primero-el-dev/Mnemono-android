package com.primeroeldev.mnemono.validation

import kotlin.reflect.KProperty1


class GameValidator : Validator
{
    private val fields: Map<String, Array<Pair<String, ((Any) -> Boolean)>>> = mapOf(
        "status" to arrayOf(
            Pair("game.validation.status.notEmpty", notEmpty)
        ),
        "includedInStatistics" to arrayOf(

        ),
        "correctAnswersCount" to arrayOf(

        ),
        "allAnswersCount" to arrayOf(

        ),
        "type" to arrayOf(

        ),
        "duration" to arrayOf(

        ),
    )

    @Suppress("UNCHECKED_CAST")
    fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
        val property = instance::class.members
            .first { it.name == propertyName } as KProperty1<Any, *>

        return property.get(instance) as R
    }

    public fun getErrorsFor(value: Any): Map<String, String?>
    {
        var errors: MutableMap<String, String?> = MutableMap()

        for (field in this.fields) {
            errors.put(field, )
        }
    }
}