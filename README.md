# <img src="https://raw.githubusercontent.com/devcrocod/Kotlin-AI-Examples/refs/heads/master/img/kotlin_icon.svg" width="30"/> Kotlin <img src="https://raw.githubusercontent.com/devcrocod/Kotlin-AI-Examples/refs/heads/master/img/AI_icon.svg" width="30"/> AI Examples

**Kotlin AI Examples** is a repository showcasing various AI frameworks integrated into Kotlin-based projects.  
Here you’ll find ready-to-use examples for Spring AI, LangChain4j, as well as interactive Kotlin notebooks.

---

## Contents

<table>
  <tr>
    <td><img src="img/kotlin_icon.svg" width="15" alt=""/> <a href="#kotlin-projects-projects">Kotlin Projects</a></td>
    <td><img src="img/ktn_plugin_icon.svg" width="15" alt=""/> <a href="#kotlin-notebooks-notebooks">Kotlin Notebooks</a></td>
    <td><a href="#getting-started">🚀 Getting Started</a></td>
    <td><a href="#prerequisites">⚙ Prerequisites</a></td>
  </tr>
</table>

---

## Kotlin Projects ([`/projects`](projects))

This section contains complete Kotlin projects demonstrating AI integrations.

### Spring AI Examples ([`/projects/spring-ai`](projects/spring-ai))

- **[helloworld](projects/spring-ai/helloworld)**: Basic example of using Spring AI
- **[spring-ai-examples](projects/spring-ai/spring-ai-examples)**: A comprehensive set of Spring AI feature
  demonstrations and examples
- **[playground-flight-booking](projects/spring-ai/playground-flight-booking)**: An AI-powered flight booking system
  demo using multiple providers (OpenAI, VertexAI Gemini, Azure OpenAI, Groq, Anthropic Claude)
- **[spring-ai-mcp-server-example](projects/spring-ai/spring-ai-mcp-server-example)**: spring mcp server sample
- **[springAI-demo](projects/spring-ai/springAI-demo)**: a Spring Boot application with Spring AI and Kotlin that loads
  Kotlin standard library documents into a Qdrant vector store, implements endpoints for similarity search and a
  RAG-powered chat interface, and integrates an LLM for detailed, document-driven answers.

### LangChain4j Examples ([`/projects/langchain4j`](projects/langchain4j))

- **[langchain4j-spring-boot](projects/langchain4j/langchain4j-spring-boot)**: Examples of using LangChain4j with Kotlin
  and Spring

### MCP Examples ([`/projects/mcp`](projects/mcp))

- **[mcp-demo](projects/mcp/mcp-demo)**: this tutorial briefly describes the process of creating an MCP server in Kotlin
  to work with stock data from FMP, connecting it to Claude for Desktop,
  and developing a custom client based on OpenAI using Compose.

---

## Kotlin Notebooks ([`/notebooks`](notebooks))

A collection of interactive Jupyter notebooks in Kotlin, organized by project.

### Spring AI Kotlin Notebooks ([`/notebooks/spring-ai`](notebooks/spring-ai))

- **[SpringAI_Overview.ipynb](notebooks/spring-ai/SpringAI_Overview.ipynb)**: overview of Spring AI with Kotlin

- **[Tutorial Series](notebooks/spring-ai/tutorials)**
  This series of interactive notebooks provides a hands-on journey through Spring AI capabilities with Kotlin.
  Each notebook builds on the previous one, guiding you from basic concepts to advanced AI integrations.

  | Notebook                                                                                   | Description                                                                     |
      |--------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|
  | **[1. Intro](notebooks/spring-ai/tutorials/1.%20Intro.ipynb)**                             | Introduction to Spring AI fundamentals and setup with Kotlin                    |
  | **[2. Prompts](notebooks/spring-ai/tutorials/2.%20Prompts.ipynb)**                         | Working with prompt engineering techniques and best practices                   |
  | **[3. Streaming](notebooks/spring-ai/tutorials/3.%20Streaming.ipynb)**                     | Implementing streaming responses for real-time AI interactions                  |
  | **[4. Tools](notebooks/spring-ai/tutorials/4.%20Tools.ipynb)**                             | Connecting LLMs to external tools and functions for enhanced capabilities       |
  | **[5. Structured Outputs](notebooks/spring-ai/tutorials/5.%20Structured%20Outputs.ipynb)** | Getting formatted, structured data from LLMs for easier application integration |
  | **[6. Advisors](notebooks/spring-ai/tutorials/6.%20Advisors.ipynb)**                       | Using Spring AI's Advisor system to enhance and customize AI interactions       |
  | **[7. RAG](notebooks/spring-ai/tutorials/7.%20RAG.ipynb)**                                 | Implementing Retrieval-Augmented Generation with your own data sources          |
  | **[8. Text-to-Image](notebooks/spring-ai/tutorials/8.%20text-to-image.ipynb)**             | Converting text descriptions into generated images with Spring AI               |
  | **[9. Text-to-Audio](notebooks/spring-ai/tutorials/9.%20text-to-audio.ipynb)**             | Creating speech from text and transcribing audio with AI models                 |
  | **[10. Local Model](notebooks/spring-ai/tutorials/10.%20Local%20model.ipynb)**             | Running AI models locally on your machine using Ollama and Spring AI            |

### AI Agents Kotlin Notebooks ([`/notebooks/agents`](notebooks/agents))

| Notebook                                                                           | Description                                                                                                                                 |
|------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| **[Prompt Chaining](notebooks/agents/Prompt-Chaining%20Workflow.ipynb)**           | Implements a sequential workflow that breaks complex tasks into simpler steps, where each LLM call processes the output of the previous one |
| **[Routing](notebooks/agents/Routing%20Workflow.ipynb)**                           | Shows how to classify inputs and direct them to specialized handlers, optimizing for different types of requests                            |
| **[Parallelization](notebooks/agents/Parallelization%20Workflow.ipynb)**           | Demonstrates running multiple LLM tasks simultaneously using Kotlin coroutines for improved performance                                     |
| **[Orchestrator-Workers](notebooks/agents/Orchestrator-Workers%20Workflow.ipynb)** | Implements a pattern where a central LLM breaks down tasks dynamically and delegates them to specialized worker LLMs                        |
| **[Evaluator-Optimizer](notebooks/agents/Evaluator-Optimizer%20Workflow.ipynb)**   | Creates a feedback loop where one LLM generates responses and another evaluates them for continuous improvement                             |

### LangChain4j Kotlin Notebooks ([`/notebooks/langchain4j`](notebooks/langchain4j))

- **[LangChain4j_Overview.ipynb](notebooks/langchain4j/LangChain4j_Overview.ipynb)**: overview of LangChain4j with
  Kotlin, demonstrating how to work with chat models, manage prompts, streaming, and produce structured responses.
- **[SummarizingDocuments.ipynb](notebooks/langchain4j/SummarizingDocuments.ipynb)**: demonstrates how to split large
  text documents into smaller chunks, summarize each chunk with an AI model, and then merge the summaries into a concise
  final result.

### Agents ReaCtor (ARC) Kotlin Notebooks ([`/notebooks/arc`](notebooks/arc))

- **[WeatherAgent.ipynb](notebooks/arc/WeatherAgent.ipynb)**: demonstrates creating an agent that retrieves real-time
  weather data for a specified location via WeatherAPI
- **[SummarizerAgent.ipynb](notebooks/arc/SummarizerAgent.ipynb)**: demonstrates creating an agent that summarizes web
  pages (e.g., blog posts) by processing HTML and generating concise summaries

### KInference Kotlin Notebooks ([`/notebooks/kInference`](notebooks/kinference))

- **[KIClassification.ipynb](notebooks/kinference/KIClassification.ipynb)**: how to set up a classification environment
  using the KIEngine framework, manage cached data files, and perform inference on input data.
- **[KIGPT2.ipynb](notebooks/kinference/KIGPT2.ipynb)**: how to configure and run a GPT-2 model with KIEngine, handling
  tokenization, caching, and text generation.
- **[ORTClassification.ipynb](notebooks/kinference/ORTClassification.ipynb)**: how to perform classification using
  ORT-based inference, handling data loading, caching, and model execution steps.
- **[ORTGPT2.ipynb](notebooks/kinference/ORTGPT2.ipynb)**: how to run GPT-2 inference using the ORT engine, handling
  tokenization, caching, and generation of text outputs.

### xef.ai Kotlin Notebooks ([`/notebooks/xefAI`](notebooks/xefAI))

- **[xefAI_Overview.ipynb](notebooks/xefAI/xefAI_Overview.ipynb)**: xef.ai overview notebook

---

## Getting Started

Each project in the [`projects`](projects) directory has its own README with detailed instructions on how to run and use
the examples.

---

## Prerequisites

- Java 17+
- Kotlin
- Appropriate AI provider API keys (see each project’s README for more details)

---

> **Note**  
> Make sure you have the necessary access credentials for your chosen AI service (OpenAI, Azure, VertexAI, etc.) and the
> required dependencies in your build scripts (Gradle/Maven).

<p align="center">
  ⭐ If you enjoy this repository, please give it a star!
</p>

---

## License

This project is licensed under the [Apache License 2.0](LICENSE).
