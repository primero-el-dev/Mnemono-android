package com.primeroeldev.mnemono.validation

import kotlin.reflect.KProperty1


fun getErrorsOf(validators: Map<String, Array<Pair<String, ((Any) -> Boolean)>>>): (Any) -> Map<String, String>
{
    return { value: Any ->
        var errors: MutableMap<String, String> = MutableMap()

        for (field in this.fields) {
            val prop = readInstanceProperty(value, field)

            for ((error, validator) in validators.get(field)!!) {
                if (!validator(prop)) {
                    errors.put(field, error)
                    break
                }
            }
        }

        return (Map<String, String>) errors
    }
}


@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.members
        .first { it.name == propertyName } as KProperty1<Any, *>

    return property.get(instance) as R
}

fun isType(type: String): (Any) -> Boolean
{
    return { value: Any -> (value != null) && (value::class.java.typeName === type) }
}

fun notEmpty(value: Any): Boolean
{
    return value !== null
        || (value is String && value !== "")
        || (value is Int && value !== 0)
}

fun minLength(length: Int): (String) -> Boolean
{
    return { value: String -> value.length >= length }
}

fun maxLength(length: Int): (String) -> Boolean
{
    return { value: String -> value.length <= length }
}

fun min(min: Float): (Float) -> Boolean
{
    return { value: Float -> min <= value }
}

fun max(max: Float): (Float) -> Boolean
{
    return { value: Float -> max >= value }
}

fun pattern(regex: String): (String) -> Boolean
{
    return { value: String -> value.matches(regex.toRegex()) }
}

fun inArray(array: Array<Any>): (Any) -> Boolean
{
    return { value: Any -> array.contains(value) }
}