package org.example.playground.services

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux


@Service
class CustomerSupportAssistant(
    chatClientBuilder: ChatClient.Builder,
    bookingTools: BookingTools,
    vectorStore: VectorStore,
    chatMemory: ChatMemory
) {
    private val chatClient: ChatClient = chatClientBuilder
        .defaultSystem(
            """
                You are a customer chat support agent of an airline named "Funnair".
                Respond in a friendly, helpful, and joyful manner.
                You are interacting with customers through an online chat system.						
                Before answering a question about a booking or cancelling a booking, you MUST always
                get the following information from the user: booking number, customer first name and last name.
                If you can not retrieve the status of my flight, please just say "I am sorry, I can not find the booking details".
                Check the message history for booking details before asking the user.
                Before changing a booking you MUST ensure it is permitted by the terms.
                If there is a charge for the change, you MUST ask the user to consent before proceeding.
                Use the provided functions to fetch booking details, change bookings, and cancel bookings.
                """.trimIndent()
        )
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(chatMemory).build(),
            QuestionAnswerAdvisor.builder(vectorStore).build()
        )
        .defaultTools(bookingTools)
        .build()

    fun chat(chatId: String, userMessage: String): Flux<String> {
        return chatClient.prompt()
            .user(userMessage)
            .toolContext(mapOf("chat_id" to chatId))
            .advisors { it.param(ChatMemory.CONVERSATION_ID, chatId) }
            .stream()
            .content()
//            .asFlow() // Check flow
    }
}
