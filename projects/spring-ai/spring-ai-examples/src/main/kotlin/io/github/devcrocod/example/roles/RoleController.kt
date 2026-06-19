package io.github.devcrocod.example.roles

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

//@RestController
//public class RoleController {
//
//    private final ChatClient chatClient;
//
//    @Value("classpath:/prompts/system-message.st")
//    private Resource systemResource;
//
//    @Autowired
//    public RoleController(ChatClient chatClient) {
//        this.chatClient = chatClient;
//    }
//
//    @GetMapping("/ai/roles")
//    public AssistantMessage generate(@RequestParam(value = "message",
//        defaultValue = "Tell me about three famous pirates from the Golden Age of Piracy and why they did.  Write at least a sentence for each pirate.") String message,
//    @RequestParam(value = "name", defaultValue = "Bob") String name,
//    @RequestParam(value = "voice", defaultValue = "pirate") String voice) {
//        UserMessage userMessage = new UserMessage(message);
//        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);
//        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));
//        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
//        return chatClient.call(prompt).getResult().getOutput();
//    }
//
//}

@RestController
class RoleController(private val chatClient: ChatClient) {

    @Value("classpath:/prompts/system-message.st")
    private lateinit var systemResource: Resource

    @GetMapping("/ai/roles")
    fun generate(
        @RequestParam(
            value = "message",
            defaultValue = "Tell me about three famous pirates from the Golden Age of Piracy and why they did.  Write at least a sentence for each pirate."
        ) message: String,
        @RequestParam(value = "name", defaultValue = "Bob") name: String,
        @RequestParam(value = "voice", defaultValue = "pirate") voice: String
    ): AssistantMessage {
        val userMessage = UserMessage(message)
        val systemPromptTemplate = SystemPromptTemplate(systemResource)
        val systemMessage = systemPromptTemplate.createMessage(mapOf("name" to name, "voice" to voice))
        val prompt = Prompt(listOf(userMessage, systemMessage))
        return chatClient.prompt(prompt).call().chatResponse()!!.result!!.output
    }
}