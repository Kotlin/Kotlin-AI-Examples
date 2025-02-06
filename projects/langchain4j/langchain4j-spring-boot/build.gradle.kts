plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "2.1.10"
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "io.github.devcrocod.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

val langchain4jVersion = "1.0.0-beta1"
val langchain4jKotlinVersion = "0.1.7"
val coroutinesVersion = "1.7.3"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("me.kpavlov.langchain4j.kotlin:langchain4j-kotlin:$langchain4jKotlinVersion")
    implementation("dev.langchain4j:langchain4j-spring-boot-starter:$langchain4jVersion")
    implementation("dev.langchain4j:langchain4j-open-ai-spring-boot-starter:$langchain4jVersion")
    implementation("dev.langchain4j:langchain4j-reactor:$langchain4jVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
