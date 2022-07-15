package com.primeroeldev.mnemono.general

import kotlin.reflect.KProperty1


@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.members
        .first { it.name == propertyName } as KProperty1<Any, *>

    return property.get(instance) as R
}


fun String.toCamelCase() =
    split('_').joinToString("", transform = String::uppercase)
