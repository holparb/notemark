package com.holparb.notemark.core.presentation.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Instant.toReadableDate(): String {
    val year = this.atZone(ZoneId.systemDefault()).year
    val currentYear = Instant.now().atZone(ZoneId.systemDefault()).year
    val formatter = if(year == currentYear) {
        DateTimeFormatter.ofPattern("dd MMM")
    } else {
        DateTimeFormatter.ofPattern("dd MMM yyyy")
    }
    return this.atZone(ZoneId.systemDefault()).format(formatter)
}

fun Instant.toISO8601DateTimeString(): String {
    return this.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}