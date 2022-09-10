package com.primeroeldev.mnemono.general

import android.widget.DatePicker
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
            val calendar = Calendar.getInstance()

            return String.format(
                "%d-%s-%s %s:%s:%s",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH).toString().padStart(2, '0'),
                calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0'),
                calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0'),
                calendar.get(Calendar.MINUTE).toString().padStart(2, '0'),
                calendar.get(Calendar.SECOND).toString().padStart(2, '0')
            )
        }

        fun getFormattedDatePickerValue(picker: DatePicker): String
        {
            return picker.year.toString() +
                "-" + (picker.month + 1).toString().padStart(2, '0') +
                "-" + picker.dayOfMonth.toString().padStart(2, '0')
        }
    }
}