package com.primeroeldev.mnemono.validation


class ValidationException(message: String) : Exception(message)


interface Validator
{
    fun getErrorsFor(value: Any): Map<String, String?>
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

//fun inArray(array: Array<Any>): (Any) -> Boolean
//{
//    return { value: Any -> array.contains(value) }
//}