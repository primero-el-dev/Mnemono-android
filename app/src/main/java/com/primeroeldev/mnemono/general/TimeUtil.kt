package com.primeroeldev.mnemono.general

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TimeUtil
{
    companion object
    {
        fun longToTimeString(millis: Long): String
        {
            val seconds = ((millis / 1000) % 60).toString()
            val minutes = ((millis / 60000) % 60).toString()
            val hours = (millis / 3600000).toString()

            return "${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}:${seconds.padStart(2, '0')}"
        }

        fun getCurrentDateTimeFormated(): String
        {
            return DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now())
        }
    }
}