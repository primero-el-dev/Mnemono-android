package com.primeroeldev.mnemono.general

import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties


fun Boolean.toInt() = if (this) 1 else 0


@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R
{
    val field = instance::class.java.getDeclaredField(propertyName)

    field.isAccessible = true

    return field.get(instance) as R
}


fun writeInstanceProperty(instance: Any, property: String, value: Any)
{
    writeInstanceProperties(instance, listOf(Pair(property, value)))
}


fun writeInstanceProperties(obj: Any, fieldsToChange: List<Pair<String, Any?>>)
{
    fieldsToChange.forEach { (propertyName, propertyValue) ->
        obj::class.members
            .filter { prop -> prop.visibility == KVisibility.PUBLIC }
            .filter { prop -> prop.name == propertyName }
            .filterIsInstance<KMutableProperty<*>>()
            .forEach { prop -> prop.setter.call(obj, propertyValue) }
    }
}


fun String.safeSubstring(startIndex: Int, endIndex: Int): String
{
    var start = minOf(startIndex, endIndex)
    var end = maxOf(startIndex, endIndex)

    if (start < 0) {
        start = 0
    }
    else if (start > this.length) {
        start = this.length
    }

    if (end < 0) {
        end = 0
    }
    else if (end > this.length) {
        end = this.length
    }

    return this.substring(start, end)
}