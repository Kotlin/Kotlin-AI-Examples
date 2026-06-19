package org.example.playground.client

import com.vaadin.flow.server.auth.AnonymousAllowed
import com.vaadin.hilla.BrowserCallable
import org.example.playground.data.BookingDetails
import org.example.playground.services.FlightBookingService

@BrowserCallable
@AnonymousAllowed
class BookingService(private val flightBookingService: FlightBookingService) {

    fun getBookings(): List<BookingDetails> =
        flightBookingService.getBookings()
}