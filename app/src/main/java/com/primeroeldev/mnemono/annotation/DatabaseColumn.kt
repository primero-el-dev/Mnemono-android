package com.primeroeldev.mnemono.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


@Target(AnnotationTarget.FIELD)
@Retention(RetentionPolicy.RUNTIME)
annotation class DatabaseColumn(
    val columnName: String = "",
    val dataType: String = "TEXT",
    val defaultValue: String = "",
    val length: Int = 0,
    val canBeNull: Boolean = true,
    val id: Boolean = false
)
