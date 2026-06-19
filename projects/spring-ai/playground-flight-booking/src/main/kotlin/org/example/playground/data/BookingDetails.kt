package org.example.playground.data

import java.time.LocalDate

data class BookingDetails(
    val bookingNumber: String,
    val firstName: String,
    val lastName: String,
    val date: LocalDate?,
    val bookingStatus: BookingStatus?,
    val from: String?,
    val to: String?,
    val seatNumber: String?,
    val bookingClass: String?
)