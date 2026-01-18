package dev.summersalad.dndslop.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.PostgresChatMemoryRepositoryDialect;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class LLMClient {
    private final ChatMemory chatMemory;
    private final ChatClient chatClient;

    public LLMClient(
            final DataSource dataSource,
            final ChatModel chatModel,
            final Resource context
    ) {
        final var jdbcTemplate = new JdbcTemplate(dataSource);
        final var chatMemoryRepository = JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .dialect(new PostgresChatMemoryRepositoryDialect())
                .build();
        this.chatMemory = MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(chatMemoryRepository)
                .build();
        this.chatClient = ChatClient
                .builder(chatModel)
                .defaultSystem(context)
                .build();
    }

    public String call(final String prompt, final String conversationId) {
        final var messages = chatMemory.get(conversationId);
        return call(prompt, conversationId, messages);
    }

    public String call(final String prompt, final String conversationId, final List<Message> context) {
        final var response = this.chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .messages(context)
                .user(prompt)
                .call()
                .content();

        if (response != null && !response.isBlank()) {
            chatMemory.add(conversationId, UserMessage.builder().text(prompt).build());
            chatMemory.add(conversationId, AssistantMessage.builder().content(response).build());
        }

        return response;
    }

    public List<Message> history(final String conversationId) {
        return chatMemory.get(conversationId);
    }

    public void clear(final String conversationId) {
        chatMemory.clear(conversationId);
    }
}
