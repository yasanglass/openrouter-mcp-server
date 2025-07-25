package glass.yasan.mcp.openrouter

import glass.yasan.mcp.openrouter.client.OpenRouterClient
import glass.yasan.mcp.openrouter.client.OpenRouterClientImpl
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

const val ENV_OPENROUTER_API_KEY = "OPENROUTER_API_KEY"

const val TOOL_INPUT_MODELS = "models"
const val TOOL_INPUT_USER_MESSAGE = "user_message"

fun main() {
    val openRouterApiKey = System.getenv(ENV_OPENROUTER_API_KEY)
        ?: throw IllegalStateException("$ENV_OPENROUTER_API_KEY environment variable is not set")

    val server: Server = createServer(openRouterApiKey)
    val stdioServerTransport = StdioServerTransport(
        inputStream = System.`in`.asSource().buffered(),
        outputStream = System.out.asSink().buffered(),
    )

    runBlocking {
        val job = Job()
        server.onClose { job.complete() }
        server.connect(stdioServerTransport)
        job.join()
    }
}

fun createServer(openRouterApiKey: String): Server {
    val openRouterClient: OpenRouterClient = OpenRouterClientImpl(
        openRouterApiKey = openRouterApiKey
    )

    val info = Implementation(
        name = "OpenRouter MCP",
        version = "1.0-SNAPSHOT",
    )
    val options = ServerOptions(
        capabilities = ServerCapabilities(
            tools = ServerCapabilities.Tools(true),
        ),
    )
    val server = Server(
        serverInfo = info,
        options = options
    ).apply {
        addUserPromptTool(openRouterClient)
    }

    return server
}

fun Server.addUserPromptTool(
    openRouterClient: OpenRouterClient,
) {
    val inputScheme = Tool.Input(
        buildJsonObject {
            put(TOOL_INPUT_MODELS, buildJsonObject {
                put("type", "array")
                put("items", buildJsonObject {
                    put("type", "string")
                })
            })
            put(TOOL_INPUT_USER_MESSAGE, "string")
        }
    )

    addTool(
        name = "user-chat-completion",
        description = "Returns chat completion responses of given models from OpenRouter for the user message",
        inputSchema = inputScheme,
    ) { input ->
        val models = input.arguments[TOOL_INPUT_MODELS]!!.jsonArray.map { it.jsonPrimitive.content }.toTypedArray()
        val userMessage = input.arguments[TOOL_INPUT_USER_MESSAGE]!!.jsonPrimitive.content

        val responses = openRouterClient.userChatCompletion(
            userMessage = userMessage,
            *models,
        )

        CallToolResult(
            listOf(
                TextContent(
                    text = buildString {
                        models.zip(responses).forEach { pair ->
                            val (model, response) = pair
                            appendLine("Model: $model")
                            appendLine("Response: $response")
                            appendLine("---")
                        }
                    }
                ),
            ),
        )
    }
}