# <img src="../img/ktn_plugin_icon.svg" width="28"/> Kotlin AI Notebooks

![Kotlin](https://img.shields.io/badge/Kotlin-19191c?logo=kotlin&link=https%3A%2F%2Fkotlinlang.org)
![Kotlin Notebook](https://img.shields.io/badge/Kotlin_Notebook-19191c?logo=kotlin&link=https%3A%2F%2Fplugins.jetbrains.com%2Fplugin%2F16340-kotlin-notebook)
![License](https://img.shields.io/badge/License-Apache%202.0-4285F4)

A collection of interactive Kotlin notebooks demonstrating AI capabilities across multiple frameworks –
from Spring AI and LangChain4j to agent workflow patterns and on-device inference.

## Table of Contents

- **[Spring AI](#spring-ai)** — 12 notebooks
- **[AI Agents](#ai-agents)** — 5 notebooks
- [LangChain4j](#langchain4j) — 2 notebooks
- [OpenAI SDK](#openai-sdk) — 2 notebooks
- [ARC](#arc) — 3 notebooks
- [KInference](#kinference) — 4 notebooks
- [xef.ai](#xefai) — 1 notebook

---

## Getting Started

1. Clone the repository
2. Open in **IntelliJ IDEA** (Kotlin Notebook plugin is bundled, no extra installation needed)
3. Open any `.ipynb` file — it runs directly in the IDE

<details>
<summary><b>Jupyter setup</b></summary>

You can also use Jupyter.

1. Install the Kotlin Kernel:
   ```bash
   pip install kotlin-jupyter-kernel
   ```
2. Launch Jupyter and open any `.ipynb` file:
   ```bash
   jupyter notebook
   ```

</details>

---

## Notebooks

### Spring AI

![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0+-6DB33F)
![Notebooks](https://img.shields.io/badge/notebooks-12-4285F4)

Spring AI integration with Kotlin – from basic setup to RAG, tools, streaming, and multimodal generation.

- **[Spring AI Overview](spring-ai/SpringAI_Overview.ipynb)** – getting started with Spring AI and Kotlin

#### Tutorial Series

|  # | Notebook                                                                  | Topics                           |
|---:|---------------------------------------------------------------------------|----------------------------------|
|  1 | [Intro](spring-ai/tutorials/1.%20Intro.ipynb)                             | Fundamentals, setup              |
|  2 | [Prompts](spring-ai/tutorials/2.%20Prompts.ipynb)                         | Prompt engineering, templates    |
|  3 | [Streaming](spring-ai/tutorials/3.%20Streaming.ipynb)                     | Real-time streaming responses    |
|  4 | [Tools](spring-ai/tutorials/4.%20Tools.ipynb)                             | Function calling, external tools |
|  5 | [Structured Outputs](spring-ai/tutorials/5.%20Structured%20Outputs.ipynb) | Typed responses, data extraction |
|  6 | [Advisors](spring-ai/tutorials/6.%20Advisors.ipynb)                       | Request/response interception    |
|  7 | [RAG](spring-ai/tutorials/7.%20RAG.ipynb)                                 | Retrieval-Augmented Generation   |
|  8 | [Text-to-Image](spring-ai/tutorials/8.%20text-to-image.ipynb)             | Image generation                 |
|  9 | [Text-to-Audio](spring-ai/tutorials/9.%20text-to-audio.ipynb)             | Speech synthesis, transcription  |
| 10 | [Local Model](spring-ai/tutorials/10.%20Local%20model.ipynb)              | Ollama, local inference          |

---

### AI Agents

![Spring AI-based](https://img.shields.io/badge/Spring%20AI--based-6DB33F)
![Coroutines](https://img.shields.io/badge/Coroutines-7F52FF)
![Notebooks](https://img.shields.io/badge/notebooks-5-4285F4)

Five workflow patterns for building AI agent systems with Kotlin coroutines and Spring AI.

- **[Prompt Chaining](agents/Prompt-Chaining%20Workflow.ipynb)** – sequential steps where each LLM call processes the
  previous output
- **[Routing](agents/Routing%20Workflow.ipynb)** – classify inputs and direct them to specialized handlers
- **[Parallelization](agents/Parallelization%20Workflow.ipynb)** – run multiple LLM tasks simultaneously with
  coroutines
- **[Orchestrator-Workers](agents/Orchestrator-Workers%20Workflow.ipynb)** – a central LLM breaks down tasks and
  delegates to workers
- **[Evaluator-Optimizer](agents/Evaluator-Optimizer%20Workflow.ipynb)** – feedback loop where one LLM generates and
  another evaluates

---

### LangChain4j

![LangChain4j](https://img.shields.io/badge/LangChain4j-1C1E24)
![Notebooks](https://img.shields.io/badge/notebooks-2-4285F4)

- **[LangChain4j Overview](langchain4j/LangChain4j_Overview.ipynb)** – chat models, prompts, streaming, and structured
  responses
- **[Document Summarization](langchain4j/SummarizingDocuments.ipynb)** – split, summarize, and merge large documents

---

### OpenAI SDK

![OpenAI](https://img.shields.io/badge/OpenAI-412991?logo=openai&logoColor=white)
![Notebooks](https://img.shields.io/badge/notebooks-2-4285F4)

- **[SDK Overview](openai/OpenAI%20Java%20SDK%20Overview.ipynb)** – client setup, API calls, prompt techniques, and
  function calling
- **[Image Recognition](openai/OpenAI%20Image%20Recognition.ipynb)** – analyze images with GPT-4o vision capabilities

---

### ARC

![ARC](https://img.shields.io/badge/ARC-F57C00)
![Notebooks](https://img.shields.io/badge/notebooks-3-4285F4)

ARC framework is function-calling agents for weather, tasks, and web summaries.

- **[Weather Agent](arc/WeatherAgent.ipynb)** – real-time weather data retrieval via WeatherAPI
- **[Task Manager Agent](arc/TaskManagerAgent.ipynb)** – conversational task management with function calling
- **[Summarizer Agent](arc/SummarizerAgent.ipynb)** – summarize web pages by processing HTML content

---

### KInference

![On-device](https://img.shields.io/badge/on--device-gray)
![Notebooks](https://img.shields.io/badge/notebooks-4-4285F4)

On-device ML inference with KIEngine and ONNX Runtime backends – no API keys required.

- **[KI Classification](kinference/KIClassification.ipynb)** – classification with KIEngine
- **[KI GPT-2](kinference/KIGPT2.ipynb)** – text generation with GPT-2 on KIEngine
- **[ORT Classification](kinference/ORTClassification.ipynb)** – classification with ONNX Runtime
- **[ORT GPT-2](kinference/ORTGPT2.ipynb)** – text generation with GPT-2 on ONNX Runtime

---

### xef.ai

![Experimental](https://img.shields.io/badge/experimental-lightgray)

- **[xef.ai Overview](xefAI/xefAI_Overview.ipynb)** – getting started with xef.ai

---

## Prerequisites

- **Java 17+**
- AI provider API keys (see [API Keys](#api-keys) below)

### API Keys

> **Note**
> Set the required environment variables before running notebooks:
> ```bash
> export OPENAI_API_KEY="your-key"
> ```

| Section     | Required Variables    |
|-------------|-----------------------|
| Spring AI   | `OPENAI_API_KEY`      |
| AI Agents   | `OPENAI_API_KEY`      |
| LangChain4j | `OPENAI_API_KEY`      |
| OpenAI SDK  | `OPENAI_API_KEY`      |
| ARC         | `OPENAI_API_KEY`      |
| KInference  | _None (runs locally)_ |
| xef.ai      | `OPENAI_API_KEY`      |

---

<p align="center">
  For complete Kotlin projects, see the <a href="../README.md">root README</a>.
</p>
