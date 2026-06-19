package org.example.playground.data

data class Customer(
    var firstName: String,
    var lastName: String,
    var bookings: MutableList<Booking> = mutableListOf()
)