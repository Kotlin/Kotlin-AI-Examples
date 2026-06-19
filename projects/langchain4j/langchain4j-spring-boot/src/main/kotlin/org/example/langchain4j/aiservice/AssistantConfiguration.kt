package org.example.langchain4j.aiservice

import dev.langchain4j.memory.ChatMemory
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AssistantConfiguration {

    /**
     * This chat memory will be used by an [Assistant]
     */
    @Bean
    fun chatMemory(): ChatMemory = MessageWindowChatMemory.withMaxMessages(10)
}