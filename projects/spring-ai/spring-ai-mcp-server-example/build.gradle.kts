plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    application
}

application {
    mainClass.set("MainKt")
}

group = "io.github.devcrocod.example"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.ai:spring-ai-mcp-server-spring-boot-starter:1.0.0-M6")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}