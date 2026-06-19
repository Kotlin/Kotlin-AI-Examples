package org.example.langchain4j

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<org.example.langchain4j.Application>(*args)
}