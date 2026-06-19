package io.github.devcrocod.example.playground

import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.lumo.Lumo
import io.micrometer.observation.ObservationPredicate
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.reader.TextReader
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.http.server.observation.ServerRequestObservationContext

@SpringBootApplication
// Vaadin 25 removed @Theme; load Lumo base styles + the Lumo utility classes
// (the views use utility classes like `flex`, `gap-m`, `p-m`, `font-bold`).
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
class Application : AppShellConfigurator {
    /** Index the Terms-of-Service document into the vector store. */
    @Bean
    fun ingestTermOfServiceToVectorStore(
        vectorStore: VectorStore,
        @Value("classpath:rag/terms-of-service.txt") termsOfServiceDocs: Resource
    ): CommandLineRunner = CommandLineRunner {
        vectorStore.write(
            TokenTextSplitter
                .builder()
                .build()
                .transform(TextReader(termsOfServiceDocs).read())
        )
    }

    /** Default in-memory chat window. */
    @Bean
    fun chatMemory(): ChatMemory =
        MessageWindowChatMemory.builder().build()

    /**
     * Optionally suppress actuator and static-asset endpoints from micrometer
     * `http.server.requests` observations
     */
    @Bean
    fun noActuatorServerObservations(): ObservationPredicate =
        ObservationPredicate { name, context ->
            if (name == "http.server.requests" && context is ServerRequestObservationContext) {
                val uri = context.carrier?.requestURI ?: return@ObservationPredicate true
                val excludedPrefixes = listOf("/actuator", "/VAADIN", "/HILLA", "/connect", "/**")
                excludedPrefixes.none { uri.startsWith(it) } && !uri.equals("/", ignoreCase = true)
            } else {
                true
            }
        }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
