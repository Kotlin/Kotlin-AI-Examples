package org.example

import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

    @Bean
    fun chatClient(builder: ChatClient.Builder): ChatClient = builder.build()
}