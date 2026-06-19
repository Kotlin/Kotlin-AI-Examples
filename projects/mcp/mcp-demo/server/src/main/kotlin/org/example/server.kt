package org.example

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.util.collections.*
import io.ktor.utils.io.streams.*
import io.modelcontextprotocol.kotlin.sdk.server.*
import io.modelcontextprotocol.kotlin.sdk.types.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.buffered
import kotlinx.serialization.json.*

/**
 * Configures and initializes a server instance with predefined tools and capabilities.
 *
 * The server is set up to interact with financial data APIs and provides tools for retrieving
 * real-time stock prices and historical price data. It uses an HTTP client configured with JSON
 * support to perform requests to external data sources.
 *
 * @return The configured [Server] instance with tools for handling financial data operations.
 */
fun configureServer(): Server {
    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }
    val httpClient = HttpClient {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "financialmodelingprep.com"
                path("api/v3/")
            }
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) { json(json) }
        install(Logging) { logger = Logger.DEFAULT }
    }

    val server = Server(
        Implementation(
            name = "mcp-kotlin Financial Modeling Prep server",
            version = "1.0.0"
        ),
        ServerOptions(
            capabilities = ServerCapabilities(
                prompts = ServerCapabilities.Prompts(listChanged = true),
                resources = ServerCapabilities.Resources(subscribe = true, listChanged = true),
                tools = ServerCapabilities.Tools(listChanged = true)
            )
        )
    )

    server.addTool(
        name = "get-current-price",
        description = """
            The full-quote quote endpoint provides the latest bid and ask prices for a stock,
            as well as the volume and last trade price.
            Investors can use this information to get a sense of what a stock is trading at in real time and to make informed investment decisions.
            
            For example, an investor may want to use the full-quote quote endpoint to get a sense of what a stock is trading at before placing a trade.
            Or, an investor may want to use the full-quote quote endpoint to track the performance of a stock over time.
        """.trimIndent(),
        inputSchema = ToolSchema(
            properties = JsonObject(mapOf("symbol" to JsonPrimitive("string"))),
            required = listOf("symbol")
        )
    ) { request ->
        val symbol = request.arguments?.get("symbol") ?: return@addTool CallToolResult(
            content = listOf(TextContent("The 'symbol' parameter is required."))
        )
        val price = httpClient.getCurrentPrice(symbol.jsonPrimitive.content)
        if (price != null) {
            CallToolResult(
                content = listOf(
                    TextContent(
                        """
                            Current data for ${price.name}:
                            $price
                        """.trimIndent()
                    )
                )
            )
        } else {
            CallToolResult(
                content = listOf(TextContent("Failed to retrieve data for $symbol"))
            )
        }
    }

    server.addTool(
        name = "get-historical-price",
        description = """
            The FMP Daily Chart endpoint is a valuable tool for investors who need to track the performance of a stock over a period of time.
            Daily charts can be used to identify trends in the stock price and to identify support and resistance levels.
            The FMP Daily Chart endpoint is easy to use.
            Simply provide the ticker symbol for the company that you are interested in and the date range that you want to view.
            The endpoint will return a daily chart for the company that shows the opening price, high price, low price,
            and closing price of the company's stock for each day in the specified date range.
            Investors can use the FMP Daily Chart endpoint to identify trends in the stock price by looking for patterns in the chart.
            
            For example, an investor might look for stocks that are in an uptrend or a downtrend.
            Investors can also use the FMP Daily Chart endpoint to identify support and resistance levels.
        """.trimIndent(),
        inputSchema = ToolSchema(
            properties = JsonObject(
                mapOf(
                    "symbol" to JsonPrimitive("string"),
                    "from" to JsonPrimitive("date"),
                    "to" to JsonPrimitive("date"),
                )
            ),
            required = listOf("symbol")
        )
    ) { request ->
        val symbol = request.arguments?.get("symbol")
        val from = request.arguments?.get("from")?.jsonPrimitive?.contentOrNull
        val to = request.arguments?.get("to")?.jsonPrimitive?.contentOrNull
        if (symbol == null) {
            return@addTool CallToolResult(
                content = listOf(TextContent("The 'symbol' parameter is required."))
            )
        }
        val price = httpClient.getHistoricalPrice(symbol.jsonPrimitive.content, from, to)
        if (price != null) {
            CallToolResult(
                content = listOf(
                    TextContent(
                        """
                            Historical data for ${price.symbol}:
                            ${price.historical}
                        """.trimIndent()
                    ),
                )
            )
        } else {
            CallToolResult(
                content = listOf(TextContent("Failed to retrieve data for $symbol"))
            )
        }
    }

    return server
}

/**
 * Runs an MCP (Model Context Protocol) server using standard I/O for communication.
 *
 * This function initializes a server instance configured with predefined tools and capabilities.
 * It sets up a transport mechanism using standard input and output for communication.
 * Once the server starts, it listens for incoming connections, processes requests,
 * and executes the appropriate tools. The server shuts down gracefully upon receiving
 * a close event.
 */
fun runMcpServerUsingStdio() {
    val server = configureServer()
    val transport = StdioServerTransport(
        System.`in`.asInput(),
        System.out.asSink().buffered()
    )

    runBlocking {
        server.createSession(transport)
        val done = Job()
        server.onClose {
            done.complete()
        }
        done.join()
    }
}

/**
 * Launches an SSE (Server-Sent Events) MCP (Model Context Protocol) server on the specified port.
 * This server enables clients to connect via SSE for real-time communication and provides endpoints
 * for handling specific messages.
 *
 * @param port The port number on which the SSE server should be started.
 */
fun runSseMcpServer(port: Int): Unit = runBlocking {
    val serverSessions = ConcurrentMap<String, ServerSession>()

    val server = configureServer()

    embeddedServer(CIO, host = "127.0.0.1", port = port) {
        install(SSE)
        routing {
            sse("/sse") {
                val transport = SseServerTransport("/message", this)
                val serverSession = server.createSession(transport)
                serverSessions[transport.sessionId] = serverSession

                server.onClose {
                    serverSessions.remove(transport.sessionId)
                }
            }
            post("/message") {
                val sessionId = call.request.queryParameters["sessionId"]
                if (sessionId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing sessionId parameter")
                    return@post
                }

                val transport = serverSessions[sessionId]?.transport as? SseServerTransport
                if (transport == null) {
                    call.respond(HttpStatusCode.NotFound, "Session not found")
                    return@post
                }

                transport.handlePostMessage(call)
            }
        }
    }.start(wait = true)
}
