package glass.yasan.mcp.openrouter.client

interface OpenRouterClient {

     suspend fun userChatCompletion(
         userMessage: String,
         vararg models: String,
    ): List<String>

}