package org.example

import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.core.JsonValue
import com.openai.models.*
import com.openai.models.chat.completions.*
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.sse.*
import io.ktor.serialization.kotlinx.json.*
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.mcpSse
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.Tool
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.jvm.optionals.getOrNull

/**
 * Converts a list of `Tool` objects into a corresponding list of `ChatCompletionTool` objects.
 * Each `Tool` object is transformed into a `ChatCompletionTool` by building its `FunctionDefinition`
 * and associating it with the defined parameters.
 *
 * @return A list of `ChatCompletionTool` objects created based on the input list of `Tool` objects.
 */
private fun List<Tool>.toListChatCompletionTools(): List<ChatCompletionTool> {
    return this.map { tool ->
        val functionTool = ChatCompletionFunctionTool.builder()
            .function(
                FunctionDefinition.builder()
                    .name(tool.name)
                    .description(tool.description ?: "")
                    .parameters(
                        FunctionParameters.builder()
                            .putAdditionalProperty("type", JsonValue.from(tool.inputSchema.type))
                            .putAdditionalProperty("properties", JsonValue.from(tool.inputSchema.properties))
                            .putAdditionalProperty("required", JsonValue.from(tool.inputSchema.required))
                            .build()
                    )
                    .build()
            )
            .build()
        ChatCompletionTool.ofFunction(functionTool)
    }
}

/**
 * A client for interacting with the MCP (Model Context Protocol) server.
 * Handles connection establishment, tool management, and query processing using OpenAI models.
 */
class MCPClient : AutoCloseable {
    lateinit var mcpClient: Client
    lateinit var httpClient: HttpClient
    lateinit var availableTools: List<ChatCompletionTool>

    // Configures using the `OPENAI_API_KEY`, `OPENAI_ORG_ID` and `OPENAI_PROJECT_ID` environment variables
    val openAiClient: OpenAIClient = OpenAIOkHttpClient.fromEnv()

    val serverVersion: Implementation
        get() = mcpClient.serverVersion ?: error("Server version is not available")

    /**
     * Establishes a connection to the specified server using Server-Sent Events (SSE).
     * Configures the HTTP client with content negotiation for JSON and SSE capabilities.
     * After establishing the connection, it retrieves and processes a list of available tools on the server.
     *
     * @param serverUrl The URL of the server to connect to.
     */
    suspend fun connectToServer(serverUrl: String) {
        httpClient = HttpClient {
            install(SSE) {
                showCommentEvents()
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; prettyPrint = true })
            }
        }

        // Connect to the MCP server via SSE. The mcpSse function performs a handshake.
        mcpClient = httpClient.mcpSse(serverUrl)
        println("Connected to the MCP server: ${mcpClient.serverVersion}")

        // Getting a list of available tools
        val tools = mcpClient.listTools().tools
        availableTools = tools.toListChatCompletionTools()
        println("Available tools: ${tools.map { it.name }}")
    }

    /**
     * Processes a user query by interacting with the OpenAI API and supported tools.
     * Generates a response by calling tools as needed and appending the results to the conversation.
     *
     * @param query The user-provided input to be processed.
     * @return The formatted response obtained from the conversational model and tool usage.
     */
    suspend fun processQuery(query: String): String {
        // Create the base conversation message
        val messages = mutableListOf(
            // System message
            ChatCompletionMessageParam.ofSystem(
                ChatCompletionSystemMessageParam.builder()
                    .content("You are a financial specialist. You can analyze financial data and make forecasts.")
                    .build(),
            ),
            // User message
            ChatCompletionMessageParam.ofUser(
                ChatCompletionUserMessageParam.builder().content(query).build()
            )
        )

        // Set up parameters for the OpenAI chat completion
        val params = ChatCompletionCreateParams.builder()
            .messages(messages)
            .model(ChatModel.GPT_4O)
            .maxCompletionTokens(1000)
            .tools(availableTools)
            .build()

        // Get hte initial completion response
        val completion = openAiClient.chat().completions().create(params)

        val answer = StringBuilder()
        val assistantMessage = StringBuilder()

        // Handle all choices from the assistant
        for (choice in completion.choices()) {
            // Append direct content from the assistant
            choice.message().content().ifPresent {
                answer.appendLine(it)
                assistantMessage.appendLine(it)
            }
            // Process any tool calls returned by the assistant
            for (toolCall in choice.message().toolCalls().getOrNull() ?: emptyList()) {
                val functionToolCall = toolCall.asFunction()
                val toolName = functionToolCall.function().name()
                val args = Json {}.decodeFromString<Map<String, String>>(functionToolCall.function().arguments())

                // Call the MCP tool with parsed arguments
                val result = mcpClient.callTool(toolName, args)

                val aiMessage = "Calling tool $toolName with arguments $args"
                answer.appendLine("[$aiMessage]")
                assistantMessage.appendLine(aiMessage)

                // Add the assistant message to reflect the tool call
                messages.add(
                    ChatCompletionMessageParam.ofAssistant(
                        ChatCompletionAssistantMessageParam.builder().content(assistantMessage.toString()).build()
                    )
                )

                // Add the user message with the tool result
                messages.add(
                    ChatCompletionMessageParam.ofUser(
                        ChatCompletionUserMessageParam.builder()
                            .content(
                                """
                                    "type": "tool_result",
                                    "tool_use_id": ${functionToolCall.id()},
                                    "result": ${result.content.joinToString()}
                                """.trimIndent()
                            )
                            .build()
                    )
                )

                // Request a new response after the tool usage
                val params = ChatCompletionCreateParams.builder()
                    .messages(messages)
                    .model(ChatModel.GPT_4O)
                    .maxCompletionTokens(1000)
                    .tools(availableTools)
                    .build()

                // Append the additional content from the new response
                val response = openAiClient.chat().completions().create(params)

                answer.appendLine(response.choices().firstOrNull()?.message()?.content()?.getOrNull() ?: "")
            }
        }

        return answer.toString()
    }


    /**
     * Closes the resources used by the `MCPClient` instance.
     */
    override fun close() = runBlocking {
        mcpClient.close()
        httpClient.close()
    }
}