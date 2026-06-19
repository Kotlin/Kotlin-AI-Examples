package org.example.playground.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap


/**
 * Manages seat-change requests flowing between the chat agent and the UI.
 */
@Service
class SeatChangeQueue {

    /** Simple “envelope” for a seat-change request. */
    data class SeatChangeRequest(val requestId: String)

    /** Futures that are awaiting a seat choice keyed by chat/session ID. */
    val pendingRequests = ConcurrentHashMap<String, CompletableFuture<String>>()

    /** Reactive sinks that broadcast seat-change requests to subscribers, keyed by chat/session ID. */
    val seatChangeRequests = ConcurrentHashMap<String, Sinks.Many<SeatChangeRequest>>()
}