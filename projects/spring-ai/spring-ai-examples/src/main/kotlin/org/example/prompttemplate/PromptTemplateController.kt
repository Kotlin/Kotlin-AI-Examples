package org.example.prompttemplate

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RestController
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@RestController
class PromptTemplateController(private val chatClient: ChatClient) {
    @Value("classpath:/prompts/joke-prompt.st")
    lateinit var jokeResource: Resource

    @GetMapping("/ai/prompt")
    fun completion(
        @RequestParam(value = "adjective", defaultValue = "funny") adjective: String,
        @RequestParam(value = "topic", defaultValue = "cows") topic: String
    ): AssistantMessage {
        val promptTemplate = PromptTemplate(jokeResource)
        val prompt = promptTemplate.create(mapOf("adjective" to adjective, "topic" to topic))
        return chatClient.prompt(prompt).call().chatResponse()!!.result!!.output
    }
}

