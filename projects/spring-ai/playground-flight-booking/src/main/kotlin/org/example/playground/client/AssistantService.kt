package org.example.playground.client

import com.vaadin.flow.server.auth.AnonymousAllowed
import com.vaadin.hilla.BrowserCallable
import org.example.playground.services.CustomerSupportAssistant
import org.example.playground.services.SeatChangeQueue
import org.example.playground.services.SeatChangeQueue.SeatChangeRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks


@BrowserCallable
@AnonymousAllowed
class AssistantService(
    private val assistant: CustomerSupportAssistant,
    private val seatChangeQueue: SeatChangeQueue
) {

    /** Send a user message to the chat assistant and stream its response. */
    fun chat(chatId: String, userMessage: String): Flux<String> =
        assistant.chat(chatId, userMessage)

    /** Reactive stream of seat-change requests for the given chat session. */
    fun seatChangeRequests(chatId: String): Flux<SeatChangeRequest> =
        seatChangeQueue.seatChangeRequests
            .computeIfAbsent(chatId) { Sinks.many().unicast().onBackpressureBuffer() }
            .asFlux()

    /** Completes a pending seat-change request with the chosen seat. */
    fun completeSeatChangeRequest(requestId: String, seat: String) {
        seatChangeQueue.pendingRequests
            .remove(requestId)
            ?.complete(seat)
    }
}
