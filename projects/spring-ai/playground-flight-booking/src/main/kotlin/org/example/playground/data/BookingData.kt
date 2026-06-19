package org.example.playground.data

data class BookingData(
    var customers: List<Customer> = emptyList(),
    var bookings: List<Booking> = emptyList()
)