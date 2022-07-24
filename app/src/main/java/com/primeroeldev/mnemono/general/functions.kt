package com.primeroeldev.mnemono.general

import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties


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
