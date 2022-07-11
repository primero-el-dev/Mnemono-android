package com.primeroeldev.mnemono.validation

import kotlin.reflect.KProperty1


fun getErrorsOf(validators: Map<String, Array<Pair<String, ((Any) -> Boolean)>>>): (Any) -> MutableMap<String, String>
{
    return fun(value: Any): MutableMap<String, String> {
        val errors: MutableMap<String, String> = mutableMapOf()

        for (field in validators.keys) {
            val prop = readInstanceProperty<Any>(value, field)

            for ((error, validator) in validators.get(field)!!) {
                if (!validator(prop)) {
                    errors.put(field, error)
                    break
                }
            }
        }

        return errors
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

fun min(min: Double): (Double) -> Boolean
{
    return { value: Double -> min <= value }
}

fun max(max: Double): (Double) -> Boolean
{
    return { value: Double -> max >= value }
}

fun pattern(regex: String): (String) -> Boolean
{
    return { value: String -> value.matches(regex.toRegex()) }
}

fun inArray(array: Array<Any>): (Any) -> Boolean
{
    return { value: Any -> array.contains(value) }
}
