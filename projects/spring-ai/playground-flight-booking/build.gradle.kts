plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.vaadin") version "24.9.6"
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
    maven { url = uri("https://maven.vaadin.com/vaadin-prereleases") }
}

val vaadinVersion = "24.9.6"
val springAiVersion = "1.1.0"
val coroutinesVersion = "1.10.2"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(platform("org.springframework.ai:spring-ai-bom:$springAiVersion"))
    implementation(platform("com.vaadin:vaadin-bom:$vaadinVersion"))

    /* ----------------------------- Spring AI ------------------------------ */
    implementation("org.springframework.ai:spring-ai-starter-model-openai")
    /* --------------------------- Vector Stores ---------------------------- */
    implementation("org.springframework.ai:spring-ai-starter-vector-store-chroma")
    implementation("org.springframework.ai:spring-ai-advisors-vector-store")

    /* ------------------------------ Vaadin -------------------------------- */
    implementation("com.vaadin:vaadin-spring-boot-starter")

    /* --------------------------- Spring Starters -------------------------- */
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    /* ---------------------------- Observability --------------------------- */
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    /* ------------------------------ Logging ------------------------------- */
    implementation("com.github.loki4j:loki-logback-appender:2.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${coroutinesVersion}")

    /* ------------------------------ Testing ------------------------------- */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

vaadin {
    productionMode = project.hasProperty("production")
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    jvmArgs(
        "-Xdebug",
        "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5247"
    )
}

defaultTasks("bootRun")