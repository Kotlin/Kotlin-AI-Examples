package io.github.devcrocod.example.rag

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.document.Document
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.reader.JsonReader
import org.springframework.ai.vectorstore.SimpleVectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class RagService(
    private val chatClient: ChatClient,
    private val embeddingClient: EmbeddingModel
) {
    @Value("classpath:/data/bikes.json")
    private lateinit var bikesResource: Resource

    @Value("classpath:/prompts/system-qa.st")
    private lateinit var systemBikePrompt: Resource

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(RagService::class.java)
    }

    fun retrieve(message: String): AssistantMessage {
        // Step 1 - Load JSON document as Documents
        logger.info("Loading JSON as Documents")
        val jsonReader = JsonReader(bikesResource, "name", "price", "shortDescription", "description")
        val documents = jsonReader.get()
        logger.info("JSON loaded as Documents")

        // Step 2 - Create embeddings and save to vector store
        logger.info("Creating Embeddings...")
        val vectorStore = SimpleVectorStore.builder(embeddingClient).build()
        vectorStore.add(documents)
        logger.info("Embeddings created.")

        // Step 3 retrieve related documents to query
        logger.info("Retrieving relevant documents")
        val similarDocuments = vectorStore.similaritySearch(message)!!
        logger.info("Found ${similarDocuments.size} relevant documents.")

        // Step 4 Embed documents into SystemMessage with the `system-qa.st` prompt
        // template
        val systemMessage = getSystemMessage(similarDocuments)
        val userMessage = UserMessage(message)

        // Step 5 - Ask the AI model
        logger.info("Asking AI model to reply to question.")
        val prompt = Prompt(listOf(systemMessage, userMessage))
        logger.info(prompt.toString())

        val chatResponse = chatClient.prompt(prompt).call().chatResponse()!!
        logger.info("AI responded.")

        logger.info(chatResponse.result?.output?.text)
        return chatResponse.result?.output!!
    }

    private fun getSystemMessage(similarDocuments: List<Document>): Message {
        val documents = similarDocuments.joinToString("\n") { it.text ?: "" }
        val systemPromptTemplate = SystemPromptTemplate(systemBikePrompt)
        return systemPromptTemplate.createMessage(mapOf("documents" to documents))
    }
}