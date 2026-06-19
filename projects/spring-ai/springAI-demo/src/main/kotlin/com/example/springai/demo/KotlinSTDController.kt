package com.example.springai.demo

import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Data class representing the chat request payload.
data class ChatRequest(val query: String, val topK: Int = 3)

@RestController
@RequestMapping("/kotlin")
class KotlinSTDController(
    private val chatClientBuilder: ChatClient.Builder,
    private val restTemplate: RestTemplate,
    private val vectorStore: VectorStore,
) {

    // Logger for debugging and tracing actions.
    private val logger = LoggerFactory.getLogger(this::class.java)

    // Build the chat client with a simple logging advisor.
    private val chatClient = chatClientBuilder.defaultAdvisors(SimpleLoggerAdvisor()).build()

    @OptIn(ExperimentalUuidApi::class)
    @PostMapping("/load-docs")
    fun load() {
        // List of topics to load from the Kotlin website documentation.
        val kotlinStdTopics = listOf(
            "collections-overview", "constructing-collections", "iterators", "ranges", "sequences",
            "collection-operations", "collection-transformations", "collection-filtering", "collection-plus-minus",
            "collection-grouping", "collection-parts", "collection-elements", "collection-ordering",
            "collection-aggregate", "collection-write", "list-operations", "set-operations",
            "map-operations", "read-standard-input", "opt-in-requirements", "scope-functions", "time-measurement",
        )
        // Base URL for the documents.
        val url = "https://raw.githubusercontent.com/JetBrains/kotlin-web-site/refs/heads/master/docs/topics/"
        // Retrieve each document from the URL and add it to the vector store.
        kotlinStdTopics.forEach { topic ->
            val data = restTemplate.getForObject<String>("$url$topic.md")
            data?.let {
                val doc = Document.builder()
                    // Build a Document with a random UUID, the text content, and metadata.
                    .id(Uuid.random().toString())
                    .text(it)
                    .metadata("topic", topic)
                    .build()
                vectorStore.add(listOf(doc))
                logger.info("Document $topic loaded.")
            } ?: logger.warn("Failed to load document for topic: $topic")
        }
    }

    @GetMapping("docs")
    fun query(
        @RequestParam query: String = "operations, filtering, and transformations",
        @RequestParam topK: Int = 2
    ): List<Document>? {
        val searchRequest = SearchRequest.builder()
            .query(query)
            .topK(topK)
            .build()
        val results = vectorStore.similaritySearch(searchRequest)
        logger.info("Found ${results.size} documents for query: '$query'")
        return results
    }

    @PostMapping("/chat/ask")
    fun chatAsk(@RequestBody request: ChatRequest): String? {
        // Define the prompt template with placeholders {query} and {target}.
        val promptTemplate = PromptTemplate(
            """
            {query}.
            Please provide a concise answer based on the "Kotlin standard library" documentation.
        """.trimIndent()
        )

        // Create the prompt by substituting placeholders with actual values.
        val prompt: Prompt =
            promptTemplate.create(mapOf("query" to request.query))

        // Configure the retrieval advisor to augment the query with relevant documents.
        val retrievalAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
            .searchRequest(
                SearchRequest.builder()
                    .similarityThreshold(0.7)
                    .topK(request.topK)
                    .build()
            )
            .promptTemplate(promptTemplate)
            .build()

        // Send the prompt to the LLM with the retrieval advisor and get the response.
        val response = chatClient.prompt(prompt)
            .advisors(retrievalAdvisor)
            .call()
            .content()
        logger.info("Chat response generated for query: '${request.query}'")
        return response
    }
}
