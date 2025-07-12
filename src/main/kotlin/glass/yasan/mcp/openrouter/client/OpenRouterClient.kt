package glass.yasan.mcp.openrouter.client

interface OpenRouterClient {

     suspend fun userChatCompletion(
        model: String,
        userMessage: String,
    ): String

}