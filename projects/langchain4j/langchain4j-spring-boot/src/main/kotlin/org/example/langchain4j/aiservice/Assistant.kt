package org.example.langchain4j.aiservice

import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.spring.AiService

@AiService
interface Assistant {

    @SystemMessage("You are a polite assistant")
    fun chat(userMessage: String): String
}