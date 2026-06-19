package org.example.langchain4j.aiservice

import dev.langchain4j.agent.tool.Tool
import org.springframework.stereotype.Component
import java.time.LocalTime

@Component
class AssistantTools {

    /**
     * This tool is available to [Assistant]
     */
    @Tool
    fun currentTime(): String = LocalTime.now().toString()
}