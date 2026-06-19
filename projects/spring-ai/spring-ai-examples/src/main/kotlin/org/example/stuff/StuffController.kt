package org.example.stuff

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class StuffController(private val chatClient: ChatClient) {

    @Value("classpath:/docs/wikipedia-curling.md")
    private lateinit var docsToStuffResource: Resource

    @Value("classpath:/prompts/qa-prompt.st")
    private lateinit var qaPromptResource: Resource

    @GetMapping("/ai/stuff")
    fun completion(
        @RequestParam(
            value = "message",
            defaultValue = "Which athletes won the mixed doubles gold medal in curling at the 2022 Winter Olympics?"
        ) message: String,
        @RequestParam(value = "stuffit", defaultValue = "false") stuffit: Boolean
    ): Completion {
        val promptTemplate = PromptTemplate(qaPromptResource)
        val map = mutableMapOf<String, Any>("question" to message)
        if (stuffit) {
            map["context"] = docsToStuffResource
        } else {
            map["context"] = ""
        }
        val prompt = promptTemplate.create(map)
        return Completion(chatClient.prompt(prompt).call().content())
//        return chatClient.prompt(prompt).call().entity(Completion::class.java)
    }
}