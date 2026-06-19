package org.example.langchain4j.lowlevel

import dev.langchain4j.model.chat.ChatModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * This is an example of using a [dev.langchain4j.model.chat.ChatModel], a low-level LangChain4j API
 */
@RestController
class ChatModelController(private val chatModel: ChatModel) {

    @GetMapping("/model")
    fun model(
        @RequestParam(value = "message", defaultValue = "Hello") message: String
    ): String = chatModel.chat(message)
}