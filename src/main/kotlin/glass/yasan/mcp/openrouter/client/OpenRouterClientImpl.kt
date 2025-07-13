package glass.yasan.mcp.openrouter.client

import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.models.chat.completions.ChatCompletionCreateParams
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

class OpenRouterClientImpl(
    private val openRouterApiKey: String,
) : OpenRouterClient {

    companion object {
        private const val OPENROUTER_BASE_URL = "https://openrouter.ai/api/v1"
    }

    override suspend fun userChatCompletion(
        userMessage: String,
        vararg models: String,
    ): List<String> {
        val openAIClient = getOpenAIClient(openRouterApiKey)

        val chatCompletions = models.map { model ->
            coroutineScope {
                async {
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

                    chatCompletionMessageContent ?: "Failed to get chat completion of $model from OpenRouter"
                }
            }
        }

        return chatCompletions.awaitAll()
    }

    private fun getOpenAIClient(
        openRouterApiKey: String,
    ): OpenAIClient = OpenAIOkHttpClient.builder()
        .apiKey(openRouterApiKey)
        .baseUrl(OPENROUTER_BASE_URL)
        .build()

}
