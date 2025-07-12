package glass.yasan.mcp.openrouter.client

import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.models.chat.completions.ChatCompletionCreateParams

class OpenRouterClientImpl(
    private val openRouterApiKey: String,
) : OpenRouterClient {

    companion object {
        private const val OPENROUTER_BASE_URL = "https://api.openrouter.ai/v1"
    }

    override suspend fun userChatCompletion(
        model: String,
        userMessage: String,
    ): String {
        val openAIClient = getOpenAIClient(openRouterApiKey)

        val params = ChatCompletionCreateParams.builder()
            .addUserMessage(userMessage)
            .model(model)
            .build()

        val chatCompletion = openAIClient.chat()
            .completions()
            .create(params)
            .choices()

        val chatCompletionMessageContent = chatCompletion
            .firstOrNull()
            ?.message()
            ?.content()
            ?.orElse(null)

        return chatCompletionMessageContent ?: "Failed to get chat completion from OpenRouter"
    }

    private fun getOpenAIClient(
        openRouterApiKey: String,
    ): OpenAIClient = OpenAIOkHttpClient.builder()
        .apiKey(openRouterApiKey)
        .baseUrl(OPENROUTER_BASE_URL)
        .build()

}
