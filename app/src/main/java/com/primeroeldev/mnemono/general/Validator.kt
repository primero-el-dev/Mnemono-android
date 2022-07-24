package com.primeroeldev.mnemono.validation

import com.primeroeldev.mnemono.general.readInstanceProperty


fun getErrorsFor(validators: Map<String, Array<Pair<String, ((Any) -> Boolean)>>>): (Map<String, Any>) -> MutableMap<String, String>
{
    return fun(values: Map<String, Any>): MutableMap<String, String> {
        val errors: MutableMap<String, String> = mutableMapOf()

        for (field in validators.keys) {
            val prop = values[field]

            for ((error, validator) in validators.get(field)!!) {
                if (prop?.let { validator(it) } == true) {
                    errors.put(field, error)
                    break
                }
            }
        }

        return errors
    }
}

fun getErrorsOf(validators: Map<String, Array<Pair<String, ((Any) -> Boolean)>>>): (Any) -> MutableMap<String, String>
{
    return fun(value: Any): MutableMap<String, String> {
        val errors: MutableMap<String, String> = mutableMapOf()

        for (field in validators.keys) {
            val prop = readInstanceProperty<Any>(value, field)

            for ((error, validator) in validators[field]!!) {
                if (!validator(prop)) {
                    errors.put(field, error)
                    break
                }
            }
        }

        return errors
    }
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

fun min(min: Int): (Int) -> Boolean
{
    return { value: Int -> min <= value }
}

fun min(min: Long): (Long) -> Boolean
{
    return { value: Long -> min <= value }
}

fun min(min: Double): (Double) -> Boolean
{
    return { value: Double -> min <= value }
}

fun max(max: Int): (Int) -> Boolean
{
    return { value: Int -> max >= value }
}

fun max(max: Long): (Long) -> Boolean
{
    return { value: Long -> max >= value }
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
