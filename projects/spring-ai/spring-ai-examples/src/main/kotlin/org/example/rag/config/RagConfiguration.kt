package org.example.rag.config

import io.github.devcrocod.example.rag.RagService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RagConfiguration {

    @Bean
    fun ragService(chatClient: ChatClient, embeddingClient: EmbeddingModel): RagService =
        RagService(chatClient, embeddingClient)
}