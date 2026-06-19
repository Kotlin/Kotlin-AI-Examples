package org.example.playground.services

import org.example.playground.data.Booking
import org.example.playground.data.BookingClass
import org.example.playground.data.BookingData
import org.example.playground.data.BookingDetails
import org.example.playground.data.BookingStatus
import org.example.playground.data.Customer
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.random.Random


/**
 * In-memory flight-booking “backend” with demo data and simple business rules.
 */
@Service
class FlightBookingService {
    private val db: BookingData = BookingData()

    init {
        initDemoData()
    }

    /** Populate the database with five random demo bookings. */
    private fun initDemoData() {
        val firstNames = listOf("John", "Jane", "Michael", "Sarah", "Robert")
        val lastNames = listOf("Doe", "Smith", "Johnson", "Williams", "Taylor")
        val airportCodes =
            listOf("LAX", "SFO", "JFK", "LHR", "CDG", "ARN", "HEL", "TXL", "MUC", "FRA", "MAD", "FUN", "SJC")
        val random: Random = Random

        val customers = mutableListOf<Customer>()
        val bookings = mutableListOf<Booking>()

        repeat(5) {
            val customer = Customer(firstName = firstNames[it], lastName = lastNames[it])

            val booking = Booking(
                bookingNumber = "10${it + 1}",
                date = LocalDate.now().plusDays(2L * it),
                customer = customer,
                bookingStatus = BookingStatus.CONFIRMED,
                from = airportCodes.random(random),
                to = airportCodes.random(random),
                seatNumber = "${random.nextInt(19) + 1}A",
                bookingClass = BookingClass.entries.random(random)
            )

            customer.bookings.add(booking)
            customers += customer
            bookings += booking
        }

        // replace the database contents on every startup
        db.customers = customers
        db.bookings = bookings
    }

    fun getBookings(): List<BookingDetails> =
        db.bookings.map(::toBookingDetails)

    fun getBookingDetails(bookingNumber: String, firstName: String, lastName: String): BookingDetails =
        toBookingDetails(findBooking(bookingNumber, firstName, lastName))

    fun changeBooking(
        bookingNumber: String, firstName: String, lastName: String,
        newDate: String, from: String, to: String
    ) {
        val booking = findBooking(bookingNumber, firstName, lastName)
        require(booking.date.isAfter(LocalDate.now().plusDays(1))) {
            "Booking cannot be changed within 24 hours of the start date."
        }

        booking.apply {
            date = LocalDate.parse(newDate)
            this.from = from
            this.to = to
        }
    }

    fun cancelBooking(bookingNumber: String, firstName: String, lastName: String) {
        val booking = findBooking(bookingNumber, firstName, lastName)

        require(booking.date.isAfter(LocalDate.now().plusDays(2))) {
            "Booking cannot be cancelled within 48 hours of the start date."
        }

        booking.bookingStatus = BookingStatus.CANCELLED
    }

    fun changeSeat(bookingNumber: String, firstName: String, lastName: String, seatNumber: String) {
        findBooking(bookingNumber, firstName, lastName).seatNumber = seatNumber
    }

    private fun findBooking(bookingNumber: String, firstName: String, lastName: String): Booking =
        db.bookings.firstOrNull {
            it.bookingNumber.equals(bookingNumber, ignoreCase = true) &&
                    it.customer.firstName.equals(firstName, ignoreCase = true) &&
                    it.customer.lastName.equals(lastName, ignoreCase = true)
        } ?: throw IllegalArgumentException("Booking not found")

    private fun toBookingDetails(booking: Booking): BookingDetails =
        BookingDetails(
            booking.bookingNumber,
            booking.customer.firstName,
            booking.customer.lastName,
            booking.date,
            booking.bookingStatus,
            booking.from,
            booking.to,
            booking.seatNumber,
            booking.bookingClass.toString()
        )
}