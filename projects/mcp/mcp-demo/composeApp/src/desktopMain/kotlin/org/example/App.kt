package org.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.time.LocalTime

// Defines the role of messages
enum class MessageType {
    USER, ASSISTANT
}

// Data class for each chat message
data class ChatMessage(val type: MessageType, val content: String, val timestamp: String = getCurrentTime())

// Custom theme colors
private val DarkBlue = Color(0xFF2A3B90)
private val LightBlue = Color(0xFF4267B2)
private val LightGray = Color(0xFFF5F5F5)
private val DarkGray = Color(0xFF484848)
private val UserBubbleColor = Color(0xFF2A3B90)
private val AssistantBubbleColor = Color(0xFFF1F1F1)

private fun getCurrentTime(): String {
    val now = LocalTime.now()
    return String.format("%02d:%02d", now.hour, now.minute)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    // Input and message list states
    var query by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    // Create an MCPClient instance
    val mcpClient = remember { MCPClient() }
    // Remember the scroll state for the list
    val listState = rememberLazyListState()

    // Connecting to the server on screen startup
    LaunchedEffect(Unit) {
        mcpClient.connectToServer("http://localhost:3001")
        messages = messages + ChatMessage(
            MessageType.ASSISTANT, "👋 Hello! I'm your AI assistant. How can I help you?",
        )
    }

    suspend fun sendQuery() {
        // Avoid sending or ongoing requests
        if (query.isBlank() || isLoading) return

        isLoading = true
        messages = messages + ChatMessage(MessageType.USER, query)
        val response = mcpClient.processQuery(query)
        messages = messages + ChatMessage(MessageType.ASSISTANT, response)
        query = ""
        isLoading = false
        listState.animateScrollToItem(messages.size - 1)
    }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = DarkBlue,
            secondary = LightBlue,
            background = Color.White,
            surface = Color.White
        )
    ) {
        Scaffold(
            // App bar with a title
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(LightBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "AI",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "MCP Chat Assistant",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            content = { padding ->
                // Main container
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(LightGray)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Displays messages in a lazy list
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                state = listState,
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                items(messages) { message ->
                                    MessageItem(message)
                                }
                            }

                            // Show loading indicator
                            if (isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = DarkBlue,
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }

                        // Input field and send button
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Text field for the user's query
                                OutlinedTextField(
                                    value = query,
                                    onValueChange = { query = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .onPreviewKeyEvent { keyEvent ->
                                            if (!isLoading && keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Enter) {
                                                // Send a message on the Enter key
                                                scope.launch { sendQuery() }
                                                true
                                            } else {
                                                false
                                            }
                                        },
                                    placeholder = { Text("Enter your query...") },
                                    enabled = !isLoading,
                                    shape = RoundedCornerShape(24.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = DarkBlue,
                                        unfocusedBorderColor = Color.LightGray
                                    ),
                                    singleLine = false,
                                    maxLines = 4
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Button to send the query
                                IconButton(
                                    onClick = {
                                        if (!isLoading && query.isNotBlank()) {
                                            scope.launch { sendQuery() }
                                        }
                                    },
                                    enabled = !isLoading && query.isNotBlank(),
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(if (query.isNotBlank()) DarkBlue else Color.LightGray)
                                ) {
                                    Text(
                                        text = "Send",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    val isUserMessage = message.type == MessageType.USER

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
        ) {
            if (!isUserMessage) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(LightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "AI",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = if (isUserMessage) "You" else "Assistant",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = DarkGray
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = message.timestamp,
                fontSize = 10.sp,
                color = Color.Gray
            )

            if (isUserMessage) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(DarkBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "You",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Message bubble
        Box(
            modifier = Modifier
                .widthIn(max = if (isUserMessage) 300.dp else 500.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUserMessage) 16.dp else 4.dp,
                        bottomEnd = if (isUserMessage) 4.dp else 16.dp
                    )
                )
                .background(if (isUserMessage) UserBubbleColor else AssistantBubbleColor)
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                color = if (isUserMessage) Color.White else Color.Black
            )
        }
    }
}