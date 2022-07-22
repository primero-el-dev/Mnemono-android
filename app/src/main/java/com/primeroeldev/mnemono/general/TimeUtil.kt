package com.primeroeldev.mnemono.general

class TimeUtil
{
    companion object
    {
        fun longToTimeString(millis: Long): String
        {
            val seconds = (millis / 1000) as String
            val minutes = (millis / 60000) as String
            val hours = (millis / 3600000) as String

            return "${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}:${seconds.padStart(2, '0')}"
        }
    }
}