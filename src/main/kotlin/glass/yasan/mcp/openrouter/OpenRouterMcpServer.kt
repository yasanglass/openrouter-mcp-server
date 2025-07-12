package glass.yasan.mcp.openrouter

import glass.yasan.mcp.openrouter.client.OpenRouterClient
import glass.yasan.mcp.openrouter.client.OpenRouterClientImpl
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered

const val ENV_OPENROUTER_API_KEY = "OPENROUTER_API_KEY"

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
    )
    // TODO add tools

    return server
}
