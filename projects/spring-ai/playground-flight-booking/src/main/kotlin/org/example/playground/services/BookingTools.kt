package org.example.playground.services

import org.example.playground.data.BookingDetails
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.model.ToolContext
import org.springframework.ai.tool.annotation.Tool
import org.springframework.core.NestedExceptionUtils
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class BookingTools(
    private val flightBookingService: FlightBookingService,
    private val seatChangeQueue: SeatChangeQueue
) {
    companion object {
        private val logger = LoggerFactory.getLogger(BookingTools::class.java)
    }

    /** Get booking details, falling back to a “blank” record on error. */
    @Tool(description = "Get booking details")
    fun getBookingDetails(
        bookingNumber: String,
        firstName: String,
        lastName: String,
        toolContext: ToolContext
    ): BookingDetails =
        try {
            flightBookingService.getBookingDetails(bookingNumber, firstName, lastName)
        } catch (e: Exception) {
            logger.warn("Booking details: ${NestedExceptionUtils.getMostSpecificCause(e).message}")
            BookingDetails(bookingNumber, firstName, lastName, null, null, null, null, null, null)
        }

    /** Change a booking’s travel dates. */
    @Tool(description = "change booking dates")
    fun changeBooking(
        bookingNumber: String,
        firstName: String,
        lastName: String,
        newDate: String,
        from: String,
        to: String,
        toolContext: ToolContext
    ) {
        flightBookingService.changeBooking(bookingNumber, firstName, lastName, newDate, from, to)
    }

    /** Cancel an existing booking. */
    @Tool(description = "Cancel booking")
    fun cancelBooking(bookingNumber: String, firstName: String, lastName: String, toolContext: ToolContext) {
        flightBookingService.cancelBooking(bookingNumber, firstName, lastName)
    }

    /**
     * Initiate an interactive seat-change flow.
     * Blocks until the asynchronous seat selection is completed elsewhere.
     */
    @Tool(description = "Change seat")
    fun changeSeat(
        bookingNumber: String,
        firstName: String,
        lastName: String,
        seat: String,
        toolContext: ToolContext
    ) {
        logger.info("Changing seat for $bookingNumber to a $seat")

        val chatId = toolContext.context["chat_id"].toString()
        val future = CompletableFuture<String>()

        // Ask every sink to emit a request for this chat session
        seatChangeQueue.seatChangeRequests.values.forEach { sink ->
            sink.tryEmitNext(SeatChangeQueue.SeatChangeRequest(chatId))
        }

        val seat = try {
            future.get()          // blocks until completeSeatChangeRequest() supplies the seat
        } catch (e: Exception) {
            throw RuntimeException("Seat selection interrupted", e)
        }

        flightBookingService.changeSeat(bookingNumber, firstName, lastName, seat)
    }
}