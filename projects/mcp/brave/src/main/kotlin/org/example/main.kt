package org.example

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.StdioClientTransport
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import org.slf4j.LoggerFactory


fun main() {
    embeddedServer(CIO, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    val logger = LoggerFactory.getLogger("BraveSearchKtorApp")

    // Get the Brave Search API key from environment variables
    val braveApiKey = System.getenv("BRAVE_API_KEY")
        ?: error("The environment variable BRAVE_API_KEY is not set")

    // Starting the Brave Search process via npx
    val process = ProcessBuilder("npx", "-y", "@modelcontextprotocol/server-brave-search")
        .apply { environment()["BRAVE_API_KEY"] = braveApiKey }
        .start()

    // Create transport using the standard streams of a running process
    val transport = StdioClientTransport(
        input = process.inputStream.asSource().buffered(),
        output = process.outputStream.asSink().buffered()
    )

    // Initialize the MCP client with client information
    val client = Client(
        clientInfo = Implementation(name = "brave-client", version = "1.0.0")
    )

    // Connecting to the server (handshake is performed in connecting)
    runBlocking {
        client.connect(transport)
        logger.info("MCP Client connected. Server: ${client.serverVersion}")
    }

    // Defining Ktor routing
    routing {
        post("/chat") {
            // Get the question text from the request body
            val question = call.receiveText()
            logger.info("The following question has been received: $question")

            // Request a list of available tools from the server.
            val tools = client.listTools().tools
            if (tools.isEmpty()) {
                call.respondText("Tools not found")
                return@post
            }

            // For example, let's choose the first available tool.
            val toolName = tools.first().name

            // Call the tool, passing the "query" field with the question text as an argument
            val toolResult = client.callTool(toolName, mapOf("query" to question))
            val resultContent = toolResult.content.toString()

            // Returning the result to the client
            call.respondText(resultContent)
        }
    }
}