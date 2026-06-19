package org.example.playground.data

import java.time.LocalDate

data class Booking(
    var bookingNumber: String,
    var date: LocalDate,
    var bookingTo: LocalDate? = null,
    var customer: Customer,
    var from: String,
    var to: String,
    var bookingStatus: BookingStatus,
    var seatNumber: String,
    var bookingClass: BookingClass,
)