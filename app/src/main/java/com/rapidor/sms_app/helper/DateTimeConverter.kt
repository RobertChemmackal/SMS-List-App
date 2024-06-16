package com.rapidor.sms_app.helper

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateTimeConverter {
    companion object{
        fun timestampToDateString(timestamp: Long): String {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a")
            val instant = Instant.ofEpochMilli(timestamp)
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            return date.format(formatter)
        }

    }

}