package com.primeroeldev.mnemono.general

import android.widget.DatePicker
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

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

        fun intToTimeString(seconds: Int): String
        {
            val secs = (seconds % 60).toString()
            val minutes = ((seconds / 60) % 60).toString()
            val hours = (seconds / 3600).toString()

            return "${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}:${secs.padStart(2, '0')}"
        }

        fun getCurrentDateTimeFormated(): String
        {
            return DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(TimeZone.getDefault().toZoneId())
                .format(Instant.now())
        }

        fun getFormattedDatePickerValue(picker: DatePicker): String
        {
            return picker.year.toString() +
                "-" + (picker.month + 1).toString().padStart(2, '0') +
                "-" + picker.dayOfMonth.toString().padStart(2, '0')
        }
    }
}